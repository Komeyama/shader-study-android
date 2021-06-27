package com.komeyama.shader_study_android.ui.base

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.ext.allocateDirect

class GLTexture(
    override val drawOrder: ShortArray = shortArrayOf(0, 1, 2, 0, 2, 3),
    override val vertexCoordinates: FloatArray = floatArrayOf(
        -1.0f, 1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,
        1.0f, -1.0f, 0.0f,
        1.0f, 1.0f, 0.0f
    ),
    override val initColor: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
) : ShaderBase() {

    private val textureCoordinates: FloatArray = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    )

    private var rendererTexture = 0
    private var uMVPMatrixHandle = 0
    private var aPositionHandle = 0
    private var uTextureHandle = 0
    private var aTextureCoordinateHandle = 0

    override fun vertexShaderCode(): String {
        return "" +
                "uniform mat4 uMVPMatrix;" +
                "attribute vec4 aPosition;" +
                "attribute vec2 aTextureCoords;" +
                "varying vec2 vTextureCoords;" +
                "" +
                "void main(){" +
                "   vTextureCoords = aTextureCoords;" +
                "   gl_Position = uMVPMatrix * aPosition;" +
                "}"
    }

    override fun fragmentShaderCode(): String {
        return "" +
                "uniform sampler2D uTexture;" +
                "varying vec2 vTextureCoords;" +
                "" +
                "void main() {" +
                "   gl_FragColor = texture2D(uTexture, vTextureCoords);" +
                "}"
    }

    override fun handleFragmentParameter() {}

    private val vertexBuffer by lazy {
        vertexCoordinates.allocateDirect()
    }

    private val drawListBuffer by lazy {
        drawOrder.allocateDirect()
    }

    private val textureCoordinateBuffer by lazy {
        textureCoordinates.allocateDirect()
    }

    fun initTexture(width: Int, height: Int, texture: Int) {
        rendererTexture = texture
        val textureSize = width * height
        val textureBuffer = textureSize.allocateDirect()

        // wrapping setting
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, rendererTexture)
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        // scale setting
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR,
        )

        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            width,
            height,
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_SHORT_5_6_5,
            textureBuffer
        )

    }

    override fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        uMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix").also {
            GLES20.glUniformMatrix4fv(
                uMVPMatrixHandle, 1, false, mvpMatrix, 0
            )
        }

        aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                vertexBuffer
            )
        }

        aTextureCoordinateHandle = GLES20.glGetAttribLocation(program, "aTextureCoords").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                2,
                GLES20.GL_FLOAT,
                false,
                0,
                textureCoordinateBuffer
            )
        }

        uTextureHandle = GLES20.glGetUniformLocation(program, "uTexture").also {
            GLES20.glUniform1i(it, 0)
        }

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, rendererTexture)
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, drawOrder.size, GLES20.GL_UNSIGNED_SHORT,
            drawListBuffer
        )

        GLES20.glDisableVertexAttribArray(aPositionHandle)
        GLES20.glDisableVertexAttribArray(aTextureCoordinateHandle)

    }
}