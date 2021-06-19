package com.komeyama.shader_study_android.ui.study1

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study1SurfaceView(context: Context, attrs: AttributeSet) : GLSurfaceViewBase(context, attrs) {

    companion object {
        private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f
    }

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun getRendererInstance(): Renderer {
        return Study1Renderer()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {

        val x: Float = e.x
        val y: Float = e.y

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                var dx: Float = x - previousX
                var dy: Float = y - previousY

                // reverse direction of rotation above the mid-line
                if (y < height / 2) {
                    dx *= -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x > width / 2) {
                    dy *= -1
                }

                (renderer as Study1Renderer).angle += (dx + dy) * TOUCH_SCALE_FACTOR
                requestRender()
            }
        }

        previousX = x
        previousY = y
        return true
    }

    fun changeTriangleColor(rgba: FloatArray) {
        (renderer as Study1Renderer).changeTriangleColor(rgba[0], rgba[1], rgba[2], rgba[3])
    }

}