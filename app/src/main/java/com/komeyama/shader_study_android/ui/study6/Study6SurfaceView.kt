package com.komeyama.shader_study_android.ui.study6

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study6SurfaceView(context: Context, attrs: AttributeSet) : GLSurfaceViewBase(context, attrs) {

    private var tapPoint: PointF? = null

    override fun getRendererInstance(): Renderer {
        return Study6Renderer()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                tapPoint = PointF(e.x, e.y)
                tapPoint?.apply {
                    (renderer as Study6Renderer).dragPosition(
                        floatArrayOf(
                            2.0f * (2 * this.x - width) / width,
                            2.0f * (2 * this.x - height) / height
                        )
                    )
                }
            }
        }
        return true
    }
}