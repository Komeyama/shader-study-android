package com.komeyama.shader_study_android.ui.study2

import android.content.Context
import android.util.AttributeSet
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study2SurfaceView(context: Context, attrs: AttributeSet) : GLSurfaceViewBase(context, attrs) {
    override fun getRendererInstance(): Renderer {
        return Study2Renderer()
    }

    fun changeConversionFactor(conversionQuantity: Float) {
        (renderer as Study2Renderer).changeConversionFactor(conversionQuantity)
    }
}
