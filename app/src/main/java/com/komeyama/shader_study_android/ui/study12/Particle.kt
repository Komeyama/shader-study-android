package com.komeyama.shader_study_android.ui.study12

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.sqrt

/**
 * cf. https://wgld.org/d/webgl/w082.html
 */
class Particle(
    override val drawOrder: ShortArray = shortArrayOf(),
    override val vertexCoordinates: FloatArray = floatArrayOf(),
    override val initColor: FloatArray = floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f),
) : ShaderBase() {

    var factor = 1.0f
    var mousePosition = floatArrayOf(0.0f, 0.0f)
    var color = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)

    private var position = mutableListOf<Float>()
    private var vector = mutableListOf<Float>()
    private val velocityFactor = 0.02f
    private var resolutionX = 100
    private var resolutionY = 100
    private var intervalX = 1.0 / resolutionX
    private var intervalY = 1.0 / resolutionY
    private var verticesCount = resolutionX * resolutionY * 10

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
                "attribute vec2 v2Position;" +
                "" +
                "void main(void){" +
                "   gl_Position = uMVPMatrix * vec4(v2Position, 0.0, 1.0);" +
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
        (0 until resolutionX).forEach { rX ->
            val k = rX * resolutionX
            (0 until resolutionY).forEach { rY ->
                val l = (k + rY) * 2

                val p = vectorUpdate(
                    position[l],
                    position[l + 1],
                    mousePosition[0],
                    mousePosition[1],
                    vector[l],
                    vector[l + 1]
                )
                vector[l] = p[0]
                vector[l + 1] = p[1]
                position[l] += vector[l] * velocityFactor
                position[l + 1] += vector[l + 1] * velocityFactor
            }
        }
        setPoints()

        GLES20.glGetAttribLocation(program, "v2Position").also {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBuffers[0])
            GLES20.glBufferSubData(
                GLES20.GL_ARRAY_BUFFER,
                0,
                verticesCount,
                vertexBuffer,
            )

            GLES20.glVertexAttribPointer(
                it,
                2,
                GLES20.GL_FLOAT,
                false,
                0,
                0,
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
        (0 until resolutionX).forEach { x ->
            (0 until resolutionY).forEach { y ->
                position.add((x * intervalX * 2.0 - 1.0).toFloat())
                position.add((y * intervalY * 2.0 - 1.0).toFloat())
                vector.add(0f)
                vector.add(0f)
            }
        }
    }

    private fun vectorUpdate(
        x: Float,
        y: Float,
        tx: Float,
        ty: Float,
        vx: Float,
        vy: Float
    ): FloatArray {
        var px = tx - x
        var py = ty - y
        var r = sqrt((px * px + py * py).toDouble()) * 5.0
        if (r != 0.0) {
            px /= r.toFloat()
            py /= r.toFloat()
        }
        px += vx
        py += vy
        r = sqrt((px * px + py * py).toDouble())
        if (r != 0.0) {
            px /= r.toFloat()
            py /= r.toFloat()
        }
        return floatArrayOf(px, py)
    }
}