package com.komeyama.shader_study_android.ui.study11

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.komeyama.shader_study_android.MainActivity
import com.komeyama.shader_study_android.ui.base.ShaderBase
import com.komeyama.shader_study_android.ui.ext.allocateDirect

class Blur(
    override val drawOrder: ShortArray = shortArrayOf(0, 1, 2, 0, 2, 3),
    override val vertexCoordinates: FloatArray = floatArrayOf(
        -0.5f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f, 0.5f, 0.0f
    ),
    override val initColor: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f),
    bitmap: Bitmap,
    private val width: Int,
    private val height: Int,
) : ShaderBase() {

    private var textureCoordinates = floatArrayOf()
    private val textureCoordinateBuffer by lazy {
        textureCoordinates.allocateDirect()
    }

    init {
        loadTexture(bitmap, width, height)
        textureCoordinates = createTextureCoordinates(bitmap.width, bitmap.height)
    }

    var factor = 1.0f

    private val drawListBuffer by lazy {
        drawOrder.allocateDirect()
    }

    /**
     *  -1,1    0,1    1,1
     *           |
     *           |
     *  -1,0 -- 0,0 -- 1,0
     *           |
     *           |
     *  -1,-1   0,-1   1,-1
     *
     */
    override fun vertexShaderCode(): String {
        return "" +
                "uniform mat4 uMVPMatrix;" +
                "uniform vec2 uOffset;" +
                "attribute vec4 vPosition;" +
                "attribute vec2 aTexCoord;" +
                "" +
                "varying vec2 vTexCoord;" +
                "varying vec2 vBlurCoordinates[9];" +
                "" +
                "void main(void){" +
                "   gl_Position = uMVPMatrix * vPosition;" +
                "   vTexCoord = vec2((1.0 - aTexCoord.x), aTexCoord.y);" +
                "" +
                "   vBlurCoordinates[0] = vTexCoord.xy + vec2(-1.0, 1.0) * uOffset.st;" +
                "   vBlurCoordinates[1] = vTexCoord.xy + vec2(0.0, 1.0) * uOffset.st;" +
                "   vBlurCoordinates[2] = vTexCoord.xy + vec2(1.0, 1.0) * uOffset.st;" +
                "   vBlurCoordinates[3] = vTexCoord.xy + vec2(-1.0, 0.0) * uOffset.st;" +
                "   vBlurCoordinates[4] = vTexCoord.xy + vec2(0.0, 0.0) * uOffset.st;" +
                "   vBlurCoordinates[5] = vTexCoord.xy + vec2(1.0, 0.0) * uOffset.st;" +
                "   vBlurCoordinates[6] = vTexCoord.xy + vec2(-1.0, -1.0) * uOffset.st;" +
                "   vBlurCoordinates[7] = vTexCoord.xy + vec2(0.0, -1.0) * uOffset.st;" +
                "   vBlurCoordinates[8] = vTexCoord.xy + vec2(1.0, -1.0) * uOffset.st;" +
                "}" +
                ""
    }

    override fun fragmentShaderCode(): String {
        return "" +
                "uniform sampler2D uTexture;" +
                "" +
                "varying vec2 vTexCoord;" +
                "varying vec2 vBlurCoordinates[9];" +
                "" +
                "void main(void){" +
                "   vec3 sum = vec3(0.0);" +
                "   vec4 fragColor = texture2D(uTexture, vTexCoord);" +
                "   sum += texture2D(uTexture, vBlurCoordinates[0]).rgb * 0.04;" +
                "   sum += texture2D(uTexture, vBlurCoordinates[1]).rgb * 0.08;" +
                "   sum += texture2D(uTexture, vBlurCoordinates[2]).rgb * 0.12;" +
                "   sum += texture2D(uTexture, vBlurCoordinates[3]).rgb * 0.16;" +
                "   sum += texture2D(uTexture, vBlurCoordinates[4]).rgb * 0.20;" +
                "   sum += texture2D(uTexture, vBlurCoordinates[5]).rgb * 0.16;" +
                "   sum += texture2D(uTexture, vBlurCoordinates[6]).rgb * 0.12;" +
                "   sum += texture2D(uTexture, vBlurCoordinates[7]).rgb * 0.08;" +
                "   sum += texture2D(uTexture, vBlurCoordinates[8]).rgb * 0.04;" +
                "   gl_FragColor = vec4(sum, fragColor.a);" +
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

        GLES20.glGetUniformLocation(program, "uOffset").also {
            GLES20.glUniform2f(it, (0.25f * factor) / width, (0.25f * factor) / height)
        }

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, drawOrder.size, GLES20.GL_UNSIGNED_SHORT,
            drawListBuffer
        )
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