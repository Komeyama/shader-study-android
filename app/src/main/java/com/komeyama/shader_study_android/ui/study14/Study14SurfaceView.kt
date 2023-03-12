package com.komeyama.shader_study_android.ui.study14

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study14SurfaceView(context: Context, attrs: AttributeSet) :
    GLSurfaceViewBase(context, attrs) {

    override fun getRendererInstance(): Renderer {
        return Study14Renderer(context)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        detector.onTouchEvent(event)
        return true
    }

    private val detectListener = object : GestureDetector.OnGestureListener {
        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            (renderer as Study14Renderer).setScrollValue(p2, p3)
            return true
        }

        override fun onDown(p0: MotionEvent?): Boolean {
            return false
        }

        override fun onShowPress(p0: MotionEvent?) {}
        override fun onSingleTapUp(p0: MotionEvent?): Boolean {
            return false
        }

        override fun onLongPress(p0: MotionEvent?) {}
        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return false
        }
    }

    private val detector = GestureDetector(context, detectListener)
}