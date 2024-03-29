package com.komeyama.shader_study_android.ui.study16

import android.opengl.GLES20
import android.util.Log
import com.komeyama.shader_study_android.ui.base.ShaderBase
import com.komeyama.shader_study_android.ui.utils.ShaderUtils

class Gradation(
    override val drawOrder: ShortArray = shortArrayOf(0, 1, 2, 0, 2, 3),
    override val vertexCoordinates: FloatArray = floatArrayOf(
        -1.0f, 1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,
        1.0f, -1.0f, 0.0f,
        1.0f, 1.0f, 0.0f
    ),
    override val initColor: FloatArray = floatArrayOf(
        128f / 255f,
        128f / 255f,
        128f / 255f,
        1.0f
    )
) : ShaderBase() {

    sealed class GradationShaderType {
        data class Vertical(val ids: Int = 0) : GradationShaderType()
        data class Circle(val ids: Int = 1) : GradationShaderType()
        data class Bilinear(val ids: Int = 2) : GradationShaderType()
    }

    var color = initColor

    var resolution = floatArrayOf(0.0f, 0.0f)

    private var verticalProgram = program
    private var circleProgram = 0
    private var bilinearProgram = 1

    init {
        val vertexShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode())
        val circleFragmentShader =
            ShaderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, LinearInterpolationShaderCodes.CIRCLE)
        val bilinearFragmentShader = ShaderUtils.loadShader(
            GLES20.GL_FRAGMENT_SHADER,
            LinearInterpolationShaderCodes.BILINEAR
        )
        circleProgram = ShaderUtils.createProgram(vertexShader, circleFragmentShader)
        bilinearProgram = ShaderUtils.createProgram(vertexShader, bilinearFragmentShader)
    }

    override fun vertexShaderCode(): String {
        return "" +
                "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"
    }

    override fun fragmentShaderCode(): String {
        return LinearInterpolationShaderCodes.VERTICAL
    }

    fun setProgram(type: GradationShaderType) {
        val program = when (type) {
            is GradationShaderType.Vertical -> verticalProgram
            is GradationShaderType.Circle -> circleProgram
            is GradationShaderType.Bilinear -> bilinearProgram
        }
        GLES20.glUseProgram(program)
    }

    override fun handleFragmentParameter() {
        GLES20.glGetUniformLocation(program, "vResolution").also { resolutionHandle ->
            GLES20.glUniform2fv(resolutionHandle, 1, resolution, 0)
        }
    }
}