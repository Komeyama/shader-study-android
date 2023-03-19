package com.komeyama.shader_study_android.ui.study15

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.komeyama.shader_study_android.ui.base.GLSurfaceViewBase

class Study15SurfaceView(context: Context, attrs: AttributeSet) :
    GLSurfaceViewBase(context, attrs) {

    companion object {
        private const val TAG = "Study15SurfaceView"
    }

    override fun getRendererInstance(): Renderer {
        return Study15Renderer(context)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    private val gestureDetectListener = object : GestureDetector.OnGestureListener {
        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            (renderer as Study15Renderer).setScrollValue(p2, p3)
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

    fun createARCoreSession() {
        val session = Session(context).also {
            val config: Config = it.config
            val isDepthSupported = it.isDepthModeSupported(Config.DepthMode.AUTOMATIC)
            if (isDepthSupported) {
                config.setDepthMode(Config.DepthMode.AUTOMATIC)
            } else {
                config.setDepthMode(Config.DepthMode.DISABLED)
            }
            it.configure(config)

            try {
                it.resume()
            } catch (e: CameraNotAvailableException) {
                Log.e(TAG, e.toString())
            }
        }
        (renderer as Study15Renderer).setupSession(session)
    }

    fun setScaleFactor(scale: Float) {
        (renderer as Study15Renderer).scaleFactor = scale
    }

    private val gestureDetector = GestureDetector(context, gestureDetectListener)
}