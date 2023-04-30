package com.komeyama.shader_study_android.ui.base

import android.opengl.GLES20
import android.os.SystemClock
import com.komeyama.shader_study_android.ui.ext.allocateDirect
import com.komeyama.shader_study_android.ui.utils.ShaderUtils

abstract class ShaderBase {

    companion object {
        const val COORDINATES_PER_VERTEX = 3
        const val VERTEX_STRIDE = COORDINATES_PER_VERTEX * 4
    }

    abstract val drawOrder: ShortArray
    abstract val vertexCoordinates: FloatArray
    abstract val initColor: FloatArray

    private val startTime: Long = SystemClock.uptimeMillis()

    private val vertexBuffer by lazy {
        vertexCoordinates.allocateDirect()
    }

    private val drawListBuffer by lazy {
        drawOrder.allocateDirect()
    }

    protected var program: Int

    private var positionHandle: Int = 0
    private var vPMatrixHandle: Int = 0
    private var mColorHandle: Int = 0
    private var mTimeHandle: Int = 0

    init {
        val vertexShaderCode: String = this.vertexShaderCode()
        val fragmentShaderCode: String = this.fragmentShaderCode()
        val vertexShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = ShaderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        program = ShaderUtils.createProgram(vertexShader, fragmentShader)
        GLES20.glUseProgram(program)
    }

    abstract fun vertexShaderCode(): String
    abstract fun fragmentShaderCode(): String
    abstract fun handleFragmentParameter()

    open fun draw(mvpMatrix: FloatArray) {

        vPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix").also { mvpMatrixHandle ->
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        }

        mColorHandle = GLES20.glGetUniformLocation(program, "vColor").also { colorHandle ->
            GLES20.glUniform4fv(colorHandle, 1, initColor, 0)
        }

        mTimeHandle = GLES20.glGetUniformLocation(program, "vTime").also { timeHandle ->
            GLES20.glUniform1f(
                timeHandle,
                ((SystemClock.uptimeMillis() - startTime)).toFloat() * 0.001f
            )
        }

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition").also { positionHandle ->
            GLES20.glEnableVertexAttribArray(positionHandle)
            GLES20.glVertexAttribPointer(
                positionHandle,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                vertexBuffer
            )
        }

        handleFragmentParameter()

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawListBuffer
        )
        GLES20.glDisableVertexAttribArray(positionHandle)

    }
}