package com.komeyama.shader_study_android.ui.study1

import android.content.Context
import android.opengl.GLSurfaceView

/**
 * ref.
 * https://developer.android.com/training/graphics/opengl/environment?hl=ja
 */
class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}