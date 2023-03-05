package com.komeyama.shader_study_android.ui.study12

import android.content.Context
import android.opengl.Matrix
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import javax.microedition.khronos.opengles.GL10

class Study12Renderer(val context: Context) : GLRendererBase() {

    private var particle: Particle? = null
    private val rotationMatrix = FloatArray(16)
    private var width = 0
    private var height = 0

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        this.width = width
        this.height = height
        particle = Particle()
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        draw()
    }

    fun changeConversionFactor(conversionQuantity: Float) {
        particle?.factor = conversionQuantity
    }

    fun updateMousePosition(x: Float, y: Float) {
        val xx = (x / width) - 0.5f
        val yy = (y / height) - 0.5f
        particle?.mousePosition = floatArrayOf(-xx, -yy)
    }

    fun updateColor(color: FloatArray) {
        particle?.color = color
    }

    private fun draw() {
        val scratch = FloatArray(16)
        val angle = 0f
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0.0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
        particle?.draw(scratch)
    }
}