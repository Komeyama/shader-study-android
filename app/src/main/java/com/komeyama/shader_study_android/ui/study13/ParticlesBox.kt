package com.komeyama.shader_study_android.ui.study13

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class ParticlesBox(
    override val drawOrder: ShortArray = shortArrayOf(),
    override val vertexCoordinates: FloatArray = floatArrayOf(),
    override val initColor: FloatArray = floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f),
) : ShaderBase() {
    var factor = 1.0f
    var color = floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f)

    private var position = mutableListOf<Float>()
    private var resolutionX = 20
    private var resolutionY = 20
    private var resolutionZ = 20
    private var intervalX = 1f / resolutionX
    private var intervalY = 1f / resolutionY
    private var intervalZ = 1f / resolutionZ
    private var verticesCount = resolutionX * resolutionY * resolutionZ * 100

    private var vertexBuffer: FloatBuffer? = null
    private lateinit var vertexBuffers: IntArray

    init {
        initBuffer()
    }

    private fun setPoints() {
        vertexBuffer?.rewind()
        vertexBuffer?.put(position.toFloatArray())
        vertexBuffer?.position(0)
    }

    override fun vertexShaderCode(): String {
        return "" +
                "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
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
                "uniform vec4 vColor;" +
                "" +
                "void main(void){" +
                "   gl_FragColor = vColor;" +
                "}" +
                ""
    }

    override fun handleFragmentParameter() {
        updateColor()
        updatePoints()
    }

    private fun updatePoints() {
        setPoints()

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
        GLES20.glDrawArrays(
            GLES20.GL_POINTS, 0, position.toFloatArray().size,
        )

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    private fun updateColor() {
        GLES20.glGetUniformLocation(program, "vColor").also { colorHandle ->
            GLES20.glUniform4fv(colorHandle, 1, color, 0)
        }
    }

    private fun initBuffer() {
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

        initParticle()
    }

    private fun initParticle() {
        (-(resolutionX / 2)..resolutionX / 2).forEach { x ->
            (-(resolutionY / 2)..resolutionY / 2).forEach { y ->
                (-(resolutionZ / 2)..resolutionZ / 2).forEach { z ->
                    position.add(x * intervalX * 0.5f)
                    position.add(y * intervalY * 0.5f)
                    position.add(z * intervalZ * 0.5f)
                }
            }
        }
    }
}