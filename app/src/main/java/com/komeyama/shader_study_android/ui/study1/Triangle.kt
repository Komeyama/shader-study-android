package com.komeyama.shader_study_android.ui.study1

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase

class Triangle(
    override val drawOrder: ShortArray = shortArrayOf(0, 1, 2, 0),
    override val vertexCoordinates: FloatArray = floatArrayOf(
        0.0f, 0.577350269f, 0.0f,
        -0.5f, -0.288675135f, 0.0f,
        0.5f, -0.288675135f, 0.0f
    ),
    override val initColor: FloatArray = floatArrayOf(
        128f / 255f,
        128f / 255f,
        128f / 255f,
        1.0f
    )
) : ShaderBase() {

    var color = initColor

    override fun vertexShaderCode(): String {
        return "" +
                "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"
    }

    override fun fragmentShaderCode(): String {
        return "" +
                "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"
    }

    override fun handleFragmentParameter() {
        GLES20.glGetUniformLocation(program, "vColor").also { colorHandle ->
            GLES20.glUniform4fv(colorHandle, 1, color, 0)
        }
    }
}