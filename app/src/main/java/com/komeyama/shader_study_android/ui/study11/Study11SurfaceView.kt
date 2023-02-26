package com.komeyama.shader_study_android.ui.study11

import android.content.Context
import android.util.AttributeSet
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study11SurfaceView(context: Context, attrs: AttributeSet) :
    GLSurfaceViewBase(context, attrs) {

    override fun getRendererInstance(): Renderer {
        return Study11Renderer(context)
    }

    fun changeConversionFactor(conversionQuantity: Float) {
        (renderer as Study11Renderer).changeConversionFactor(conversionQuantity)
    }
}