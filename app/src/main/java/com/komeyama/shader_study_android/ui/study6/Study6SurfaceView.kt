package com.komeyama.shader_study_android.ui.study6

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study6SurfaceView(context: Context, attrs: AttributeSet) : GLSurfaceViewBase(context, attrs) {

    private var tapPoint: PointF = PointF(0.0f, 0.0f)
    private var mScaleFactor = 1f
    var currentPos = PointF(0.0f, 0.0f)
    var firstPos = PointF(0.0f, 0.0f)
    var lastPos = PointF(0.0f, 0.0f)

    override fun getRendererInstance(): Renderer {
        return Study6Renderer()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(e)

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                tapPoint = PointF(e.x, e.y)
                currentPos = PointF(
                    lastPos.x + (firstPos.x - tapPoint.x) / width,
                    lastPos.y + (firstPos.y - tapPoint.y) / height
                )

                (renderer as Study6Renderer).setCenterPosition(floatArrayOf(currentPos.x, - currentPos.y))
            }
            MotionEvent.ACTION_DOWN -> {
                firstPos = PointF(e.x, e.y)
            }
            MotionEvent.ACTION_UP -> {
                lastPos = currentPos
            }
        }
        return true
    }

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = 0.01f.coerceAtLeast(mScaleFactor.coerceAtMost(3.0f))
            (renderer as Study6Renderer).setScale(mScaleFactor)
            invalidate()
            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)
}