package com.komeyama.shader_study_android.ui.study17

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.komeyama.shader_study_android.MainActivity
import com.komeyama.shader_study_android.ui.base.ShaderBase
import com.komeyama.shader_study_android.ui.ext.allocateDirect

class AlphaBlend(
    override val drawOrder: ShortArray = shortArrayOf(0, 1, 2, 0, 2, 3),
    override val vertexCoordinates: FloatArray = floatArrayOf(
        -0.5f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f, 0.5f, 0.0f
    ),
    override val initColor: FloatArray = floatArrayOf(
        128f / 255f,
        128f / 255f,
        128f / 255f,
        1.0f
    ),
    val width: Int,
    val height: Int,
    val bitmap: Bitmap,
) : ShaderBase() {

    private var textureCoordinates = floatArrayOf()
    private val textureCoordinateBuffer by lazy {
        textureCoordinates.allocateDirect()
    }

    private val drawListBuffer by lazy {
        drawOrder.allocateDirect()
    }

    init {
        loadTexture(bitmap, width, height)
        textureCoordinates = createTextureCoordinates(bitmap.width, bitmap.height)
    }

    override fun fragmentShaderCode(): String {
        return "" +
                "uniform sampler2D uTexture;" +
                "" +
                "varying vec2 vTexCoord;" +
                "" +
                "void main(void){" +
                "   vec4 fragColor = texture2D(uTexture, vTexCoord);" +
                "   gl_FragColor = vec4(fragColor.rgb, 1.0);" +
                "}" +
                ""
    }

    override fun vertexShaderCode(): String {
        return "" +
                "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "attribute vec2 aTexCoord;" +
                "" +
                "varying vec2 vTexCoord;" +
                "" +
                "void main(void){" +
                "   gl_Position = uMVPMatrix * vPosition;" +
                "   vTexCoord = vec2((1.0 - aTexCoord.x), aTexCoord.y);" +
                "}" +
                ""
    }

    override fun handleFragmentParameter() {
        GLES20.glGetAttribLocation(program, "aTexCoord").also { texPositionHandle ->
            GLES20.glEnableVertexAttribArray(texPositionHandle)
            GLES20.glVertexAttribPointer(
                texPositionHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                8,
                textureCoordinateBuffer
            )
        }

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, drawOrder.size, GLES20.GL_UNSIGNED_SHORT,
            drawListBuffer
        )
    }

    fun setProgram() {
        GLES20.glUseProgram(program)
    }

    private fun createTextureCoordinates(tWidth: Int, tHeight: Int): FloatArray {
        var scaleX = 1f
        var scaleY = 1f

        if (tWidth > tHeight) {
            scaleY = tWidth.toFloat() / tHeight.toFloat()
        } else if (tWidth < tHeight) {
            scaleX = tHeight.toFloat() / tWidth.toFloat()
        }

        val x = 1f * scaleX
        val y = 1f * scaleY

        return floatArrayOf(
            0.0f, 0.0f,
            0.0f, y,
            x, y,
            x, 0.0f
        )
    }

    private fun loadTexture(bitmap: Bitmap, width: Int, height: Int) {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)
        if (textureHandle[0] != 0) {
            val wightScale = width.toFloat() / MainActivity.displayWidth.toFloat()
            val heightScale = height.toFloat() / MainActivity.displayHeight.toFloat()
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width.toFloat() * wightScale).toInt(),
                (bitmap.height.toFloat() * heightScale).toInt(),
                true
            )
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
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
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, scaledBitmap, 0)
        }
    }
}