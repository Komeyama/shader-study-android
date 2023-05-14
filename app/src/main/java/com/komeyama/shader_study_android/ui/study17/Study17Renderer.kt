package com.komeyama.shader_study_android.ui.study17

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import javax.microedition.khronos.opengles.GL10

class Study17Renderer(val context: Context) : GLRendererBase() {

    private var simpleTexture: SimpleTexture? = null
    private var simpleFilter: SimpleFilter? = null
    private val rotationMatrix = FloatArray(16)

    var angle: Float = 0f

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        drawAndRotate()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        simpleTexture = SimpleTexture(bitmap = createTargetBitmap(), width = width, height = height)
        simpleFilter = SimpleFilter(bitmap = createFilterBitmap(), width = width, height = height)
    }

    private fun drawAndRotate() {
        val scratch = FloatArray(16)
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)

        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        simpleTexture?.setProgram()
        simpleTexture?.draw(scratch)

        simpleFilter?.setProgram()
        simpleFilter?.draw(scratch)

        GLES20.glDisable(GLES20.GL_BLEND)
    }

    private fun createTargetBitmap(): Bitmap {
        val options = BitmapFactory.Options()
        return BitmapFactory.decodeResource(context.resources, R.drawable.sample_image, options)
    }

    private fun createFilterBitmap(): Bitmap {
        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    }
}