package com.komeyama.shader_study_android.ui.study2

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MyGLSurfaceView2(context: Context, attrs: AttributeSet) : GLSurfaceView(context, attrs) {

    private val renderer: MyGLRenderer2

    init {
        setEGLContextClientVersion(2)
        renderer = MyGLRenderer2()
        setRenderer(renderer)
    }

    fun changeConversionFactor(conversionQuantity: Float) {
        renderer.changeConversionFactor(conversionQuantity)
    }
}