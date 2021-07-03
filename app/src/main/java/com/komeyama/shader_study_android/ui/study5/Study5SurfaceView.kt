package com.komeyama.shader_study_android.ui.study5

import android.content.Context
import android.util.AttributeSet
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study5SurfaceView(context: Context, attrs: AttributeSet) : GLSurfaceViewBase(context, attrs) {
    override fun getRendererInstance(): Renderer {
        return Study5Renderer()
    }
}