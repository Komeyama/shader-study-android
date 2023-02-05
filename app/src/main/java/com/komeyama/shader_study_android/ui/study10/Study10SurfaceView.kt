package com.komeyama.shader_study_android.ui.study10

import android.content.Context
import android.util.AttributeSet
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study10SurfaceView(context: Context, attrs: AttributeSet) :
    GLSurfaceViewBase(context, attrs) {

    override fun getRendererInstance(): Renderer {
        return Study10Renderer()
    }
}