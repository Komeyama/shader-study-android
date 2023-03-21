package com.komeyama.shader_study_android.ui.study15

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.createScaledBitmap
import android.media.Image
import android.opengl.GLES20
import android.util.Log
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.google.ar.core.CameraIntrinsics
import com.google.ar.core.Session
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import com.komeyama.shader_study_android.ui.utils.CameraUtils
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.opengles.GL10
import kotlin.math.ceil
import kotlin.math.sqrt

class Study15Renderer(val context: Context) : GLRendererBase() {

    private val scratch = FloatArray(16)
    private var aspect = 0f

    private var theta = Math.PI / 2
    private var phi = 0.001
    private var direction = 1f
    private var rollAlignment = 0.01f
    private val distanceFromOrigin = 1.0f

    private var session: Session? = null
    private var pointCloud: PointCloud? = null
    private val maxNumberOfPointsToRender = 20000f
    var scaleFactor = 1.0f

    companion object {
        private const val TAG = "Study15Renderer"

        const val FLOATS_PER_POINT = 3
        const val BYTES_PER_FLOAT = java.lang.Float.SIZE / 8
        const val INITIAL_BUFFER_POINTS = 1000
        const val BYTES_PER_POINT = BYTES_PER_FLOAT * FLOATS_PER_POINT
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        GLES20.glViewport(0, 0, width, height)
        aspect = width.toFloat() / height
        pointCloud = PointCloud()
        pointCloud?.setup()
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        p0?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        updateFrame()
        draw()
    }

    fun setScrollValue(DeltaX: Float, DeltaY: Float) {
        theta += DeltaY * rollAlignment
        phi += DeltaX * rollAlignment * direction
    }

    fun setupSession(session: Session) {
        this.session = session
    }

    private fun draw() {
        val cameraPositionAndDirection =
            CameraUtils.changeCameraPointOfViewUpY(theta, phi, distanceFromOrigin)
        direction = cameraPositionAndDirection.direction
        CameraUtils.lookAtCameraUpY(
            scratch,
            FloatArray(16),
            FloatArray(16),
            aspect,
            cameraPositionAndDirection.position,
            cameraPositionAndDirection.direction
        )
        pointCloud?.draw(scratch)
    }

    private fun updateFrame() {
        val session = session ?: return
        try {
            session.setCameraTextureName(0)
            val frame = session.update()
            val intrinsics = frame.camera.textureIntrinsics
            session.createAnchor(frame.camera.pose).pose.toMatrix(FloatArray(16), 0)
            val depth = frame.acquireDepthImage16Bits()
            val color = frame.acquireCameraImage()
            val bitmapOrg = createBitmap(color)!!
            val bitmap = createColorBitmap(bitmapOrg, depth)
            val pointCloudBuffer = createPointCloudBuffer(depth, color, bitmap, intrinsics)
            depth.close()
            color.close()
            pointCloud?.setPositionBuffer(pointCloudBuffer[0])
            pointCloud?.setColorBuffer(pointCloudBuffer[1])
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun createColorBitmap(colorBitmap: Bitmap, depth: Image): Bitmap {
        val heightScale = colorBitmap.height / depth.height
        val widthScale = colorBitmap.width / depth.width
        return createScaledBitmap(
            colorBitmap,
            (colorBitmap.width / widthScale),
            (colorBitmap.height / heightScale),
            true
        )
    }

    private fun createPointCloudBuffer(
        depth: Image,
        color: Image,
        bitmap: Bitmap?,
        cameraTextureIntrinsics: CameraIntrinsics
    ): Array<FloatBuffer> {
        val depthByteBufferOriginal = depth.planes[0].buffer
        val depthByteBuffer = ByteBuffer.allocate(depthByteBufferOriginal.capacity())
        depthByteBuffer.order(ByteOrder.LITTLE_ENDIAN)
        while (depthByteBufferOriginal.hasRemaining()) {
            depthByteBuffer.put(depthByteBufferOriginal.get())
        }
        depthByteBuffer.rewind()
        val depthBuffer = depthByteBuffer.asShortBuffer()

        val depthWidth = depth.width
        val depthHeight = depth.height
        val depthCalibrationParameter =
            createCameraCalibrationParameter(cameraTextureIntrinsics, depthWidth, depthHeight)
        val colorCalibrationParameter =
            createCameraCalibrationParameter(cameraTextureIntrinsics, color.width, color.height)
        val step = ceil(sqrt((depthWidth * depthHeight / maxNumberOfPointsToRender))).toInt()

        val pointCloud = createColorPointCloud(
            depthBuffer,
            depthCalibrationParameter,
            colorCalibrationParameter,
            depthWidth,
            depthHeight,
            step
        )

        val colors =
            createColorBuffer(bitmap, pointCloud.colorYPixels, pointCloud.colorXPixels, step)

        return arrayOf(pointCloud.position, colors)
    }

    private fun createColorPointCloud(
        depthBuffer: ShortBuffer,
        depthParameter: CameraCalibrationParameter,
        colorParameter: CameraCalibrationParameter,
        width: Int,
        height: Int,
        step: Int
    ): ColorPointCloud {
        val positions = FloatBuffer.allocate(width / step * height / step * FLOATS_PER_POINT)
        val colorXPixels = mutableSetOf<Int>()
        val colorYPixels = mutableSetOf<Int>()
        for (y in 0 until height step step) {
            for (x in 0 until width step step) {
                val depthMillimeters = depthBuffer[x + y * width].toInt()
                if (depthMillimeters == 0) continue
                val depthMeters = depthMillimeters / 1000.0f // [mm]

                val pointX = ((depthParameter.cy - y) / depthParameter.fy) * scaleFactor
                val pointY = ((depthParameter.cx - x) / depthParameter.fx) * scaleFactor
                val pointZ = -(depthMeters * scaleFactor)
                positions.put(pointX)
                positions.put(pointY)
                positions.put(pointZ)

                colorXPixels.add((colorParameter.cx - (colorParameter.fx * pointY)).toInt())
                colorYPixels.add((colorParameter.cy - (colorParameter.fy * pointX)).toInt())
            }
        }
        positions.rewind()
        return ColorPointCloud(
            positions,
            colorYPixels.toList(),
            colorXPixels.toList()
        )
    }

    private fun createColorBuffer(
        bitmap: Bitmap?,
        yPixels: List<Int>,
        xPixels: List<Int>,
        step: Int
    ): FloatBuffer {
        val colors =
            FloatBuffer.allocate(yPixels.size / step * xPixels.size / step * FLOATS_PER_POINT)
        for (y in yPixels.indices) {
            for (x in xPixels.indices) {
                var r = 1f
                var g = 1f
                var b = 1f
                if (bitmap != null) {
                    r = bitmap.getPixel(x, y).red / 255f
                    g = bitmap.getPixel(x, y).green / 255f
                    b = bitmap.getPixel(x, y).blue / 255f
                }

                colors.put(r)
                colors.put(g)
                colors.put(b)
            }
        }

        colors.rewind()
        return colors
    }

    private fun createCameraCalibrationParameter(
        intrinsics: CameraIntrinsics,
        width: Int,
        height: Int
    ): CameraCalibrationParameter {
        val intrinsicsDimensions = intrinsics.imageDimensions
        val fx = intrinsics.focalLength[0] * width / intrinsicsDimensions[0]
        val fy = intrinsics.focalLength[1] * height / intrinsicsDimensions[1]
        val cx = intrinsics.principalPoint[0] * width / intrinsicsDimensions[0]
        val cy = intrinsics.principalPoint[1] * height / intrinsicsDimensions[1]
        return CameraCalibrationParameter(fx, fy, cx, cy)
    }

    private fun createBitmap(image: Image): Bitmap? {
        return imageToByteArray(image)?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }

    private fun imageToByteArray(image: Image): ByteArray? {
        return createJPEG(convertToNV21(image), image.width, image.height)
    }

    private fun createJPEG(nv21: ByteArray, width: Int, height: Int): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        // https://developer.android.com/reference/android/graphics/ImageFormat
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun convertToNV21(image: Image): ByteArray {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        return nv21
    }
}