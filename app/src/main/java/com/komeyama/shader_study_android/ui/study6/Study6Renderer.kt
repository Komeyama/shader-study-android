package com.komeyama.shader_study_android.ui.study6

import android.opengl.Matrix
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Study6Renderer: GLRendererBase() {

    private var mandelbrot: Mandelbrot? = null
    private val rotationMatrix = FloatArray(16)

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        super.onSurfaceCreated(p0, p1)
        mandelbrot = Mandelbrot()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        mandelbrot?.resolution = floatArrayOf(width.toFloat(), height.toFloat())
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        draw()
    }

    private fun draw() {
        val scratch = FloatArray(16)
        val angle = 0f
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
        mandelbrot?.draw(scratch)
    }

    fun dragPosition(pos: FloatArray) {
        mandelbrot?.dragPosition = pos
    }
}