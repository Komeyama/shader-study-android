package com.komeyama.shader_study_android.ui.study4

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase
import com.komeyama.shader_study_android.ui.ext.allocateDirect

class Line(
    override var drawOrder: ShortArray = shortArrayOf(0, 1, 2, 0, 2, 3),
    override var vertexCoordinates: FloatArray = floatArrayOf(
        -1.0f, 1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,
        1.0f, -1.0f, 0.0f,
        1.0f, 1.0f, 0.0f
    ),
    override var initColor: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
) : ShaderBase() {

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
                "void main(void){" +
                "   gl_FragColor = vColor;" +
                "}"
    }

    override fun handleFragmentParameter() {}

    override fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        val vertexBuffer by lazy {
            vertexCoordinates.allocateDirect()
        }

        val drawListBuffer by lazy {
            drawOrder.allocateDirect()
        }

        GLES20.glGetUniformLocation(program, "uMVPMatrix").also { mvpMatrixHandle ->
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        }

        GLES20.glGetUniformLocation(program, "vColor").also { colorHandle ->
            GLES20.glUniform4fv(colorHandle, 1, initColor, 0)
        }

        GLES20.glGetAttribLocation(program, "vPosition").also { positionHandle ->
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

        // line
        GLES20.glDrawElements(
            GLES20.GL_LINE_STRIP, drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT, drawListBuffer)
        GLES20.glLineWidth(10.5f)
    }

}