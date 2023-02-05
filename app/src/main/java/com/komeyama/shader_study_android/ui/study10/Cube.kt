package com.komeyama.shader_study_android.ui.study10

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase

class Cube(
    override val drawOrder: ShortArray = shortArrayOf(0, 1, 2, 0, 2, 3),
    override val vertexCoordinates: FloatArray = floatArrayOf(
        // surface1
        -0.2f, -0.2f, 0.2f,
        0.2f, -0.2f, 0.2f,
        -0.2f, 0.2f, 0.2f,
        0.2f, 0.2f, 0.2f,
        // surface2
        0.2f, -0.2f, -0.2f,
        -0.2f, -0.2f, -0.2f,
        0.2f, 0.2f, -0.2f,
        -0.2f, 0.2f, -0.2f,
        // surface3
        -0.2f, -0.2f, -0.2f,
        -0.2f, -0.2f, 0.2f,
        -0.2f, 0.2f, -0.2f,
        -0.2f, 0.2f, 0.2f,
        // surface4
        0.2f, -0.2f, 0.2f,
        0.2f, -0.2f, -0.2f,
        0.2f, 0.2f, 0.2f,
        0.2f, 0.2f, -0.2f,
        // surface5
        -0.2f, 0.2f, 0.2f,
        0.2f, 0.2f, 0.2f,
        -0.2f, 0.2f, -0.2f,
        0.2f, 0.2f, -0.2f,
        // surface6
        -0.2f, -0.2f, -0.2f,
        0.2f, -0.2f, -0.2f,
        -0.2f, -0.2f, 0.2f,
        0.2f, -0.2f, 0.2f,
    ),

    override val initColor: FloatArray = floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f)
) : ShaderBase() {
    var mvpMatrix = floatArrayOf()

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
                "}";
    }

    override fun handleFragmentParameter() {
        GLES20.glGetUniformLocation(program, "vColor").also { colorHandle ->
            GLES20.glUniform4f(colorHandle, 0.75f, 0.75f, 0.75f, 1.0f)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

            GLES20.glUniform4f(colorHandle, 0.8f, 0.75f, 0.75f, 1.0f)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4, 4)

            GLES20.glUniform4f(colorHandle, 0.75f, 0.8f, 0.75f, 1.0f)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 8, 4)

            GLES20.glUniform4f(colorHandle, 0.75f, 0.75f, 0.8f, 1.0f)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 12, 4)

            GLES20.glUniform4f(colorHandle, 0.7f, 0.75f, 0.75f, 1.0f)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 16, 4)

            GLES20.glUniform4f(colorHandle, 0.75f, 0.7f, 0.75f, 1.0f)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 20, 4)
        }
    }
}