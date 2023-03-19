package com.komeyama.shader_study_android.ui.study15

import android.content.Context
import android.media.Image
import android.opengl.GLES20
import android.util.Log
import com.google.ar.core.CameraIntrinsics
import com.google.ar.core.Session
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import com.komeyama.shader_study_android.ui.utils.CameraUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
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
            val pointBuffer = createPointCloudBuffer(depth, intrinsics)
            depth.close()
            pointCloud?.setPointBuffer(pointBuffer)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun createPointCloudBuffer(
        depth: Image,
        cameraTextureIntrinsics: CameraIntrinsics
    ): FloatBuffer {
        val depthByteBufferOriginal = depth.planes[0].buffer
        val depthByteBuffer = ByteBuffer.allocate(depthByteBufferOriginal.capacity())
        depthByteBuffer.order(ByteOrder.LITTLE_ENDIAN)
        while (depthByteBufferOriginal.hasRemaining()) {
            depthByteBuffer.put(depthByteBufferOriginal.get())
        }
        depthByteBuffer.rewind()

        val depthBuffer = depthByteBuffer.asShortBuffer()
        val intrinsicsDimensions = cameraTextureIntrinsics.imageDimensions
        val depthWidth = depth.width
        val depthHeight = depth.height
        val fx = cameraTextureIntrinsics.focalLength[0] * depthWidth / intrinsicsDimensions[0]
        val fy = cameraTextureIntrinsics.focalLength[1] * depthHeight / intrinsicsDimensions[1]
        val cx = cameraTextureIntrinsics.principalPoint[0] * depthWidth / intrinsicsDimensions[0]
        val cy = cameraTextureIntrinsics.principalPoint[1] * depthHeight / intrinsicsDimensions[1]

        val step = ceil(sqrt((depthWidth * depthHeight / maxNumberOfPointsToRender))).toInt()
        val points = FloatBuffer.allocate(depthWidth / step * depthHeight / step * FLOATS_PER_POINT)
        for (y in 0 until depthHeight step step) {
            for (x in 0 until depthWidth step step) {
                val depthMillimeters = depthBuffer[x + y * depthWidth].toInt()
                if (depthMillimeters == 0) continue
                val depthMeters = depthMillimeters / 1000.0f // [mm]

                val pointX = ((cy - y) / fy) * scaleFactor
                val pointY = ((cx - x) / fx) * scaleFactor
                val pointZ = - (depthMeters * scaleFactor)
                points.put(pointX)
                points.put(pointY)
                points.put(pointZ)
            }
        }

        points.rewind()
        return points
    }
}