package com.komeyama.shader_study_android.ui.study18

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Study18Renderer(val context: Context) : GLRendererBase() {

    private var stencil: Stencil? = null
    private var stencilFilter: StencilFilter? = null
    private val rotationMatrix = FloatArray(16)

    var angle: Float = 0f

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        drawAndRotate()
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        super.onSurfaceCreated(p0, p1)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        stencil = Stencil(bitmap = createTargetBitmap(), width = width, height = height)
        stencilFilter = StencilFilter(bitmap = createTargetBitmap(), width = width, height = height)
    }

    private fun drawAndRotate() {
        val scratch = FloatArray(16)
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)

        GLES20.glEnable(GLES20.GL_BLEND)
        //GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_SRC_ALPHA, GLES20.GL_ONE)

        stencil?.setProgram()
        stencil?.draw(scratch)

        stencilFilter?.setProgram()
        stencilFilter?.draw(scratch)

        GLES20.glDisable(GLES20.GL_BLEND)
    }

    private fun createTargetBitmap(): Bitmap {
        val options = BitmapFactory.Options()
        return BitmapFactory.decodeResource(context.resources, R.drawable.sample_img_b_t, options)
    }
}