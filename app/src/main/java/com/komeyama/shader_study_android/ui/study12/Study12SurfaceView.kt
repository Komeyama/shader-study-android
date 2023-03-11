package com.komeyama.shader_study_android.ui.study12

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase
import com.komeyama.shader_study_android.ui.utils.SequenceRGBChangeUtils
import com.komeyama.shader_study_android.ui.utils.dto.RGB
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class Study12SurfaceView(context: Context, attrs: AttributeSet) :
    GLSurfaceViewBase(context, attrs) {

    private var t = 0f
    private var currentRGB = RGB(255, 0, 0)

    private var downTime = 0L
    private var timeFactor = 0.0005f
    private val period = 6000
    private var upTimer = Timer()
    private var upElapsedTime = 0
    private var decreaseInterval = 100L
    private var decreaseFactor = 30f

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
                t += ((e.eventTime - downTime) * timeFactor)
                if (t > period) {
                    t = 0f
                    downTime = e.eventTime
                }
            }
            MotionEvent.ACTION_UP -> {
                decreaseColor()
            }
        }
        return true
    }

    private fun changeColor() {
        currentRGB = SequenceRGBChangeUtils.changeColor(t, currentRGB)
        val updateColor =
            floatArrayOf(currentRGB.r / 255f, currentRGB.g / 255f, currentRGB.b / 255f, 1f)
        (renderer as? Study12Renderer)?.updateColor(updateColor)
    }

    private fun decreaseColor() {
        upTimer = Timer()
        upTimer.scheduleAtFixedRate(0, decreaseInterval, upTimerTask)
    }

    private val upTimerTask = object : (TimerTask) -> Unit {
        override fun invoke(p1: TimerTask) {
            val decreaseR = currentRGB.r * (1 - (upElapsedTime / decreaseFactor))
            val decreaseG = currentRGB.g * (1 - (upElapsedTime / decreaseFactor))
            val decreaseB = currentRGB.b * (1 - (upElapsedTime / decreaseFactor))
            (renderer as? Study12Renderer)?.updateColor(
                floatArrayOf(
                    decreaseR / 255f,
                    decreaseG / 255f,
                    decreaseB / 255f,
                    1f
                )
            )
            upElapsedTime += 1
            if (upElapsedTime > 30) {
                stopTimer()
            }
        }
    }

    private fun stopTimer() {
        upTimer.cancel()
        upElapsedTime = 0
    }
}