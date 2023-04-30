package com.komeyama.shader_study_android.ui.study16

import android.opengl.Matrix
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Study16Renderer : GLRendererBase() {

    private var gradation: Gradation? = null
    private val rotationMatrix = FloatArray(16)

    var angle: Float = 0f

    private lateinit var currentType: Gradation.GradationShaderType

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        super.onSurfaceCreated(p0, p1)
        currentType = Gradation.GradationShaderType.Vertical()
        gradation = Gradation()
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        drawAndRotate()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        gradation?.resolution = floatArrayOf(width.toFloat(), height.toFloat())
    }

    fun changeFragmentShader() {
        if (currentType is Gradation.GradationShaderType.Vertical) {
            currentType = Gradation.GradationShaderType.Circle()
        } else if (currentType is Gradation.GradationShaderType.Circle) {
            currentType = Gradation.GradationShaderType.Vertical()
        }
    }

    private fun drawAndRotate() {
        val scratch = FloatArray(16)
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
        gradation?.draw(scratch)
        gradation?.setProgram(currentType)
    }
}