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

    private var colorSize = 0
    private var arrayColorBufferSize = 0
    private var arrayColorBuffer = 0
    private var colors = mutableListOf<Float>()
    private var colorAttribute = 0

    override fun vertexShaderCode(): String {
        return "" +
                "uniform mat4 uMVPMatrix;" +
                "attribute vec3 v3Position;" +
                "attribute vec3 v3Color;" +
                "varying vec4 v4Color;" +
                "" +
                "void main(void){" +
                "   v4Color = vec4(v3Color, 1.0);" +
                "   gl_Position = uMVPMatrix * vec4(v3Position, 1.0);" +
                "   gl_PointSize = 1.0;" +
                "}" +
                ""
    }

    override fun fragmentShaderCode(): String {
        return "" +
                "precision mediump float;" +
                "varying vec4 v4Color;" +
                "" +
                "void main(void){" +
                "   gl_FragColor = v4Color;" +
                "}" +
                ""
    }

    override fun handleFragmentParameter() {
        updatePointBuffer()
    }

    private fun updatePointBuffer() {
        // position
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

        // color
        GLES20.glEnableVertexAttribArray(colorAttribute)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, arrayColorBuffer)
        GLES20.glVertexAttribPointer(
            colorAttribute,
            FLOATS_PER_POINT,
            GLES20.GL_FLOAT,
            false,
            BYTES_PER_POINT,
            0
        )

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, pointSize)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, colorSize)
        GLES20.glDisableVertexAttribArray(positionAttribute)
        GLES20.glDisableVertexAttribArray(colorAttribute)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, position.toFloatArray().size)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, colors.toFloatArray().size)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    fun setup() {
        // position
        val buffers = IntArray(1)
        GLES20.glGenBuffers(1, buffers, 0)
        arrayBuffer = buffers[0]
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, arrayBuffer)

        arrayBufferSize = INITIAL_BUFFER_POINTS * BYTES_PER_POINT
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, arrayBufferSize, null, GLES20.GL_DYNAMIC_DRAW)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        positionAttribute = GLES20.glGetAttribLocation(program, "v3Position")

        // color
        val colorBuffers = IntArray(1)
        GLES20.glGenBuffers(1, colorBuffers, 0)
        arrayColorBuffer = colorBuffers[0]
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, arrayColorBuffer)

        arrayColorBufferSize = INITIAL_BUFFER_POINTS * BYTES_PER_POINT
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            arrayColorBufferSize,
            null,
            GLES20.GL_DYNAMIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        colorAttribute = GLES20.glGetAttribLocation(program, "v3Color")
    }

    fun setPositionBuffer(points: FloatBuffer) {
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

    fun setColorBuffer(colors: FloatBuffer) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, arrayColorBuffer)
        colors.rewind()
        colorSize = colors.remaining() / FLOATS_PER_POINT
        if (colorSize * BYTES_PER_FLOAT > arrayColorBufferSize) {
            while (colorSize * BYTES_PER_POINT > arrayColorBufferSize) {
                arrayColorBufferSize *= 2
            }
            GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER,
                arrayColorBufferSize,
                null,
                GLES20.GL_DYNAMIC_DRAW
            )
        }

        GLES20.glBufferSubData(
            GLES20.GL_ARRAY_BUFFER,
            0,
            colorSize * BYTES_PER_POINT,
            colors
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }
}