package com.komeyama.shader_study_android.ui.study4

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase
import com.komeyama.shader_study_android.ui.utils.RendererUtils.getLinePoints

class Study4SurfaceView(context: Context, attrs: AttributeSet) : GLSurfaceViewBase(context, attrs) {

    private var preTapPoint: PointF? = null
    private var tapPoint: PointF? = null

    override fun getRendererInstance(): Renderer {
        return Study4Renderer(context)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                tapPoint = PointF(e.x, e.y)
                if (preTapPoint == null) {
                    preTapPoint = tapPoint
                    return true
                } else {
                    val points = getLinePoints(preTapPoint!!, tapPoint!!)
                    (renderer as Study4Renderer).points = points.map {
                        PointF(
                            (2 * it.x - this.width) / this.width,
                            (2 * it.y - this.height) / this.height
                        )
                    } as MutableList<PointF>
                    preTapPoint = tapPoint
                }
            }
            MotionEvent.ACTION_UP -> {
                preTapPoint = null
            }
        }
        return true
    }

    fun clear() {
        (renderer as Study4Renderer).clear()
    }

    fun changeColor(rgb: FloatArray) {
        (renderer as Study4Renderer).changeColor(rgb)
    }
}