package com.komeyama.shader_study_android.ui.study2

import android.opengl.GLES20
import android.os.SystemClock
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Square {

    companion object {
        const val COORDS_PER_VERTEX = 3
        val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)
        val vertexCoords = floatArrayOf(
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
        )
    }

    private val color = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
    private val startTime: Long = SystemClock.uptimeMillis()
    var conversionFactor: Float = 0.0f

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexCoords.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(vertexCoords)
                position(0)
            }
        }

    private val drawListBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }
    private val vertexStride: Int = COORDS_PER_VERTEX * 4

    private var mProgram: Int
    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform float vTime;" +
                "void main(void){" +
                "   float r = abs(sin(vTime * 0.2));" +
                "   float g = abs(cos(vTime * 0.15));" +
                "   float b = abs(tan(vTime * 0.1));" +
                "   gl_FragColor = vec4(r, g, b, 1.0);" +
                "}"

    private var positionHandle: Int = 0
    private var vPMatrixHandle: Int = 0
    private var mColorHandle: Int = 0
    private var mTimeHandle: Int = 0

    init {
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram)
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {
            vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
            GLES20.glUniform4fv(colorHandle, 1, color, 0)
        }

        mTimeHandle = GLES20.glGetUniformLocation(mProgram, "vTime").also {
            GLES20.glUniform1f(it, ((SystemClock.uptimeMillis() - startTime)).toFloat() * 0.001f * conversionFactor)
        }

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawListBuffer
        )
        GLES20.glDisableVertexAttribArray(positionHandle)

    }
}