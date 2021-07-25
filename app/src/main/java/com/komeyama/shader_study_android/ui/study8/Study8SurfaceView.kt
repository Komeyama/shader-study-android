package com.komeyama.shader_study_android.ui.study8

import android.content.Context
import android.util.AttributeSet
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study8SurfaceView(context: Context, attrs: AttributeSet) : GLSurfaceViewBase(context, attrs) {

    override fun getRendererInstance(): Renderer {
        return Study8Renderer()
    }

    fun changeConversionFactor(conversionQuantity: Float) {
        (renderer as Study8Renderer).changeConversionFactor(conversionQuantity)
    }
}