package com.komeyama.shader_study_android.ui.study3

import android.content.Context
import android.util.AttributeSet
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study3SurfaceView(context: Context, attrs: AttributeSet) : GLSurfaceViewBase(context, attrs) {
    override fun getRendererInstance(): Renderer {
        return Study3Renderer()
    }

    fun changeRGB(rgb: FloatArray) {
        (renderer as Study3Renderer).changeRGB(rgb)
    }
}