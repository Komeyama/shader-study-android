package com.komeyama.shader_study_android.ui.study16

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study16SurfaceView(context: Context, attrs: AttributeSet) :
    GLSurfaceViewBase(context, attrs) {

    override fun getRendererInstance(): Renderer {
        return Study16Renderer()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> (renderer as Study16Renderer).changeFragmentShader()
        }
        return true
    }
}