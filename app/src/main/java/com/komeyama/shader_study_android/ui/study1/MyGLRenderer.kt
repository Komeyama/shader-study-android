package com.komeyama.shader_study_android.ui.study1

import android.opengl.Matrix
import com.komeyama.shader_study_android.ui.base.RenderBase
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer : RenderBase() {

    private var triangle: Triangle? = null
    private val rotationMatrix = FloatArray(16)

    var angle: Float = 0f

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        super.onSurfaceCreated(p0, p1)
        triangle = Triangle()
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        drawAndRotate()
    }

    fun changeTriangleColor(r: Float, g: Float, b: Float, a: Float) {
        triangle?.color = floatArrayOf(r / 255.0f, g / 255.0f, b / 255.0f, a)
    }

    private fun drawAndRotate() {
        val scratch = FloatArray(16)
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
        triangle?.draw(scratch)
    }
}