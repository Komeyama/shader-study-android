package com.komeyama.shader_study_android.ui.study14

import android.graphics.Bitmap
import android.opengl.GLES20
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.komeyama.shader_study_android.ui.base.ShaderBase
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class ParticlesPicture(
    override val drawOrder: ShortArray = shortArrayOf(),
    override val vertexCoordinates: FloatArray = floatArrayOf(),
    override val initColor: FloatArray = floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f),
    private val bitmap: Bitmap
) : ShaderBase() {
    var factor = 1.0f

    private var position = mutableListOf<Float>()
    private var colors = mutableListOf<Float>()
    private var resolutionX = bitmap.width
    private var resolutionY = bitmap.height
    private var intervalX = 1f / resolutionX
    private var intervalY = 1f / resolutionY
    private var verticesCount = resolutionX * resolutionY

    private var vertexBuffer: FloatBuffer? = null
    private lateinit var vertexBuffers: IntArray

    private var vertexColorBuffer: FloatBuffer? = null
    private lateinit var vertexColorBuffers: IntArray

    init {
        initBuffer()
    }

    private fun setPoints() {
        // points
        vertexBuffer?.rewind()
        vertexBuffer?.put(position.toFloatArray())
        vertexBuffer?.position(0)

        // colors
        vertexColorBuffer?.rewind()
        vertexColorBuffer?.put(colors.toFloatArray())
        vertexColorBuffer?.position(0)
    }

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
                "   gl_PointSize = 10.0;" +
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
        updatePoints()
    }

    private fun updatePoints() {
        setPoints()

        attribPosition()
        attribColor()

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, position.toFloatArray().size)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    private fun initBuffer() {
        createPositionBuffer()
        createColorBuffer()
        initParticle()
    }

    private fun attribPosition() {
        GLES20.glGetAttribLocation(program, "v3Position").also {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBuffers[0])
            GLES20.glBufferSubData(
                GLES20.GL_ARRAY_BUFFER,
                0,
                verticesCount,
                vertexBuffer,
            )

            GLES20.glVertexAttribPointer(
                it,
                3,
                GLES20.GL_FLOAT,
                false,
                0,
                0
            )

            GLES20.glEnableVertexAttribArray(it)
        }
    }

    private fun attribColor() {
        GLES20.glGetAttribLocation(program, "v3Color").also {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexColorBuffers[0])
            GLES20.glBufferSubData(
                GLES20.GL_ARRAY_BUFFER,
                0,
                verticesCount,
                vertexColorBuffer,
            )

            GLES20.glVertexAttribPointer(
                it,
                3,
                GLES20.GL_FLOAT,
                false,
                0,
                0
            )

            GLES20.glEnableVertexAttribArray(it)
        }
    }

    private fun createPositionBuffer() {
        vertexBuffer = ByteBuffer.allocateDirect(verticesCount)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexBuffer?.position(0)

        vertexBuffers = intArrayOf(1)
        GLES20.glGenBuffers(1, vertexBuffers, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBuffers[0])
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            verticesCount,
            vertexBuffer,
            GLES20.GL_STATIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    private fun createColorBuffer() {
        vertexColorBuffer = ByteBuffer.allocateDirect(verticesCount)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexColorBuffer?.position(0)

        vertexColorBuffers = intArrayOf(1)
        GLES20.glGenBuffers(1, vertexColorBuffers, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexColorBuffers[0])
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            verticesCount,
            vertexColorBuffer,
            GLES20.GL_STATIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    private fun initParticle() {
        for (x in 0 until bitmap.width step 5) {
            for (y in 0 until bitmap.width step 5) {
                val r = bitmap.getPixel(x, y).red / 255f
                val g = bitmap.getPixel(x, y).green / 255f
                val b = bitmap.getPixel(x, y).blue / 255f
                position.add(x * intervalX * 2.0f - 1.0f)
                position.add(1.0f - y * intervalY * 2.0f)
                position.add(0f)

                colors.add(r)
                colors.add(g)
                colors.add(b)
            }
        }
    }
}