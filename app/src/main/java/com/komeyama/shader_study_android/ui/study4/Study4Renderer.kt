package com.komeyama.shader_study_android.ui.study4

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.PointF
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import com.komeyama.shader_study_android.MainActivity
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import com.komeyama.shader_study_android.ui.base.GLTexture
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Study4Renderer(private val context: Context) : GLRendererBase() {

    private var line: Line? = null
    private var textured: GLTexture? = null

    private var frameBufferHandle = IntArray(1)
    private var renderTextureHandle = IntArray(1)

    private val rotationMatrix = FloatArray(16)

    var layoutSize = Point()
    var points = mutableListOf<PointF>()

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        super.onSurfaceCreated(p0, p1)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        line = Line()
        textured = GLTexture()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        layoutSize = Point(width, height)
        GLES20.glViewport(0, 0, width, height)
        homography()

        GLES20.glGenFramebuffers(1, frameBufferHandle, 0)
        renderTextureHandle[0] = loadTexture(context, R.drawable.background_texture, width, height)
        textured?.initTexture(width, height, renderTextureHandle[0])
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferHandle[0])
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER,
            GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D,
            renderTextureHandle[0],
            0
        )
        GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)

        if (points.isNotEmpty()) {
            drawLine(points)
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        cameraViewConversion()

        drawTexture()
    }

    fun clear() {
        points.clear()
        GLES20.glDeleteTextures(renderTextureHandle.size, renderTextureHandle, 0)
        renderTextureHandle[0] = loadTexture(context, R.drawable.background_texture, layoutSize.x, layoutSize.y)
    }

    fun changeColor(rgb: FloatArray) {
        points.clear()
        line?.initColor = rgb
    }

    private fun drawTexture() {
        val scratch = FloatArray(16)
        val angle = 0f
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
        textured?.draw(scratch)
    }

    private fun drawLine(p: List<PointF>) {
        val scratch = FloatArray(16)
        val angle = 0f
        val floatList = mutableListOf<Float>()
        val drawOrders = mutableListOf<Short>()
        var count = 0

        p.forEach {
            floatList.add(it.x)
            floatList.add(it.y)
            floatList.add(0.0f)
            drawOrders.add(count.toShort())
            count += 1
        }

        val floatArray = floatList.toTypedArray()
        line?.vertexCoordinates = floatArray.toFloatArray()
        line?.drawOrder = drawOrders.toTypedArray().toShortArray()

        if (drawOrders.toTypedArray().toShortArray().size > 1) {
            Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
            line?.draw(scratch)
        }
    }

    private fun loadTexture(context: Context, resourceId: Int, width: Int, height: Int): Int {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)
        if (textureHandle[0] != 0) {
            val options = BitmapFactory.Options()
            options.inScaled = false
            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)
            val wightScale = width.toFloat() / MainActivity.displayWidth.toFloat()
            val heightScale = height.toFloat() / MainActivity.displayHeight.toFloat()
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width.toFloat() * wightScale).toInt(),
                (bitmap.height.toFloat() * heightScale).toInt(),
                true
            )
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR,
            )
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, scaledBitmap, 0)
            scaledBitmap
                .recycle()
        }
        return textureHandle[0]
    }
}