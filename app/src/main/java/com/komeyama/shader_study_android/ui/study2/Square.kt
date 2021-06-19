package com.komeyama.shader_study_android.ui.study2

import android.opengl.GLES20
import android.os.SystemClock
import com.komeyama.shader_study_android.ui.base.ShaderBase

class Square(
    override val drawOrder: ShortArray = shortArrayOf(0, 1, 2, 0, 2, 3),
    override val vertexCoordinates: FloatArray = floatArrayOf(
        -0.5f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f, 0.5f, 0.0f
    ),
    override val initColor: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
) : ShaderBase() {

    var timeFactor = 0.0f
    private val startTime: Long = SystemClock.uptimeMillis()

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
                "uniform float time;" +
                "void main(void){" +
                "   float r = abs(sin(time * 0.2));" +
                "   float g = abs(cos(time * 0.15));" +
                "   float b = abs(tan(time * 0.1));" +
                "   gl_FragColor = vec4(r, g, b, 1.0);" +
                "}"
    }

    override fun handleFragmentParameter() {

        GLES20.glGetUniformLocation(program, "time").also {
            GLES20.glUniform1f(
                it,
                ((SystemClock.uptimeMillis() - startTime).toFloat() * 0.001f * timeFactor)
            )
        }
    }

}