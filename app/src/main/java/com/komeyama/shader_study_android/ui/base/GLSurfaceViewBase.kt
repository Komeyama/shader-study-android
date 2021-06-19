package com.komeyama.shader_study_android.ui.base

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

abstract class GLSurfaceViewBase(context: Context, attrs: AttributeSet) : GLSurfaceView(context, attrs) {

    protected val renderer: Renderer by lazy {
        getRendererInstance()
    }

    init {
        this.setEGLContextClientVersion(2)
        this.setRenderer(renderer)
    }

    abstract fun getRendererInstance(): Renderer
}