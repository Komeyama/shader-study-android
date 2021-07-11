package com.komeyama.shader_study_android.ui.study7

import android.content.Context
import android.util.AttributeSet
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study7SurfaceView(context: Context, attrs: AttributeSet) : GLSurfaceViewBase(context, attrs) {

    override fun getRendererInstance(): Renderer {
        return Study7Renderer()
    }
}