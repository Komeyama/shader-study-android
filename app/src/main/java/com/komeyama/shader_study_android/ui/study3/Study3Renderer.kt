package com.komeyama.shader_study_android.ui.study3

import android.opengl.Matrix
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Study3Renderer: GLRendererBase() {

    private var lightSphere: LightSphere? = null
    private val rotationMatrix = FloatArray(16)

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        super.onSurfaceCreated(p0, p1)
        lightSphere = LightSphere()
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        draw()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        lightSphere?.resolution = floatArrayOf(width.toFloat(), height.toFloat())
    }

    private fun draw() {
        val scratch = FloatArray(16)
        val angle = 0f
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
        lightSphere?.draw(scratch)
    }
}