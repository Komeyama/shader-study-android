package com.komeyama.shader_study_android.ui.study12

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class Study12SurfaceView(context: Context, attrs: AttributeSet) :
    GLSurfaceViewBase(context, attrs) {

    private var downTime = 0L
    private val period = 6000
    private var upTimer = Timer()
    private var elapsedTime = 0
    private var decreaseInterval = 100L
    private var decreaseFactor = 30f
    private val timeFactor = 0.0005
    private var r = 255f
    private var g = 0f
    private var b = 0f
    private var t = 0f

    override fun getRendererInstance(): Renderer {
        return Study12Renderer(context)
    }

    fun changeConversionFactor(conversionQuantity: Float) {
        (renderer as? Study12Renderer)?.changeConversionFactor(conversionQuantity)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                stopTimer()
                downTime = e.downTime
            }
            MotionEvent.ACTION_MOVE -> {
                (renderer as? Study12Renderer)?.updateMousePosition(e.x, e.y)
                changeColor()
                t += ((e.eventTime - downTime) * timeFactor).toFloat()
                if (t > period) {
                    t = 0f
                }
            }
            MotionEvent.ACTION_UP -> {
                decreaseColor()
            }
        }
        return true
    }

    private fun changeColor() {
        when (t) {
            in 0f..1000f -> {
                g = 255f * t / 1000f
            }
            in 1001f..2000f -> {
                r = 255f * (1f - (t / 2000f))
            }
            in 2001f..3000f -> {
                b = 255f * (t / 3000f)
            }
            in 3001f..4000f -> {
                g = 255f * (1f - (t / 4000f))
            }
            in 4001f..5000f -> {
                r = 255f * (t / 5000f)
            }
            in 5001f..6000f -> {
                b = 255f * (1f - (t / 6000f))
            }
        }
        (renderer as? Study12Renderer)?.updateColor(floatArrayOf(r / 255f, g / 255f, b / 255f, 1f))
    }

    private fun decreaseColor() {
        upTimer = Timer()
        upTimer.scheduleAtFixedRate(0, decreaseInterval, upTimerTask)
    }

    private val upTimerTask = object : (TimerTask) -> Unit {
        override fun invoke(p1: TimerTask) {
            val decreaseR = r * (1 - (elapsedTime / decreaseFactor))
            val decreaseG = g * (1 - (elapsedTime / decreaseFactor))
            val decreaseB = b * (1 - (elapsedTime / decreaseFactor))
            (renderer as? Study12Renderer)?.updateColor(
                floatArrayOf(
                    decreaseR / 255f,
                    decreaseG / 255f,
                    decreaseB / 255f,
                    1f
                )
            )
            elapsedTime += 1
            if (elapsedTime > 30) {
                stopTimer()
            }
        }
    }

    private fun stopTimer() {
        upTimer.cancel()
        elapsedTime = 0
    }
}