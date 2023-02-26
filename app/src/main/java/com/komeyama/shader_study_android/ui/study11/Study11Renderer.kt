package com.komeyama.shader_study_android.ui.study11

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Matrix
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import javax.microedition.khronos.opengles.GL10

class Study11Renderer(val context: Context) : GLRendererBase() {

    private var blur: Blur? = null
    private val rotationMatrix = FloatArray(16)
    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        blur = Blur(bitmap = createBitmap(), width = width, height = height)
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        draw()
    }

    fun changeConversionFactor(conversionQuantity: Float) {
        blur?.factor = conversionQuantity
    }

    private fun draw() {
        val scratch = FloatArray(16)
        val angle = 0f
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0.0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
        blur?.draw(scratch)
    }

    private fun createBitmap(): Bitmap {
        val options = BitmapFactory.Options()
        return BitmapFactory.decodeResource(context.resources, R.drawable.sample_image, options)
    }
}