package com.komeyama.shader_study_android.ui.study15

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase
import com.komeyama.shader_study_android.ui.study15.Study15Renderer.Companion.BYTES_PER_FLOAT
import com.komeyama.shader_study_android.ui.study15.Study15Renderer.Companion.BYTES_PER_POINT
import com.komeyama.shader_study_android.ui.study15.Study15Renderer.Companion.FLOATS_PER_POINT
import com.komeyama.shader_study_android.ui.study15.Study15Renderer.Companion.INITIAL_BUFFER_POINTS
import java.nio.FloatBuffer

class PointCloud(
    override val drawOrder: ShortArray = shortArrayOf(),
    override val vertexCoordinates: FloatArray = floatArrayOf(),
    override val initColor: FloatArray = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f),
) : ShaderBase() {
    private var pointSize = 0
    private var arrayBufferSize = 0
    private var arrayBuffer = 0

    private var position = mutableListOf<Float>()
    private var positionAttribute = 0

    override fun vertexShaderCode(): String {
        return "" +
                "uniform mat4 uMVPMatrix;" +
                "attribute vec3 v3Position;" +
                "" +
                "void main(void){" +
                "   gl_Position = uMVPMatrix * vec4(v3Position, 1.0);" +
                "   gl_PointSize = 1.0;" +
                "}" +
                ""
    }

    override fun fragmentShaderCode(): String {
        return "" +
                "precision mediump float;" +
                "" +
                "void main(void){" +
                "   gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);" +
                "}" +
                ""
    }

    override fun handleFragmentParameter() {
        updatePointBuffer()
    }

    private fun updatePointBuffer() {
        GLES20.glEnableVertexAttribArray(positionAttribute)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, arrayBuffer)
        GLES20.glVertexAttribPointer(
            positionAttribute,
            FLOATS_PER_POINT,
            GLES20.GL_FLOAT,
            false,
            BYTES_PER_POINT,
            0
        )

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, pointSize)
        GLES20.glDisableVertexAttribArray(positionAttribute)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, position.toFloatArray().size)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    fun setup() {
        val buffers = IntArray(1)
        GLES20.glGenBuffers(1, buffers, 0)
        arrayBuffer = buffers[0]
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, arrayBuffer)

        arrayBufferSize = INITIAL_BUFFER_POINTS * BYTES_PER_POINT
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, arrayBufferSize, null, GLES20.GL_DYNAMIC_DRAW)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)

        positionAttribute = GLES20.glGetAttribLocation(program, "v3Position")
    }

    fun setPointBuffer(points: FloatBuffer) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, arrayBuffer)
        points.rewind()
        pointSize = points.remaining() / FLOATS_PER_POINT
        if (pointSize * BYTES_PER_FLOAT > arrayBufferSize) {
            while (pointSize * BYTES_PER_POINT > arrayBufferSize) {
                arrayBufferSize *= 2
            }
            GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER,
                arrayBufferSize,
                null,
                GLES20.GL_DYNAMIC_DRAW
            )
        }

        GLES20.glBufferSubData(
            GLES20.GL_ARRAY_BUFFER,
            0,
            pointSize * BYTES_PER_POINT,
            points
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }
}