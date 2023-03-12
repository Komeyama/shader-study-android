package com.komeyama.shader_study_android.ui.study13

import android.content.Context
import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import com.komeyama.shader_study_android.ui.utils.CameraUtils
import com.komeyama.shader_study_android.ui.utils.SequenceRGBChangeUtils
import com.komeyama.shader_study_android.ui.utils.SequenceRGBChangeUtils.period
import com.komeyama.shader_study_android.ui.utils.dto.RGB
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.concurrent.scheduleAtFixedRate

class Study13Renderer(val context: Context) : GLRendererBase() {

    private var particlesBox: ParticlesBox? = null
    private val scratch = FloatArray(16)

    private var aspect = 0f

    private var theta = Math.PI / 2
    private var phi = Math.PI / 2
    private var rollAlignment = 0.01f
    private var direction = 1f
    private val distanceFromOrigin = 1.5f

    private var timer = Timer()
    private var elapsedTime = 0f
    private var currentRGB = RGB(255, 0, 0)

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        super.onSurfaceCreated(p0, p1)
        particlesBox = ParticlesBox()
        timer.scheduleAtFixedRate(0, 1L, upTimerTask)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        GLES20.glViewport(0, 0, width, height)
        aspect = width.toFloat() / height
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        p0?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        val cameraPositionAndDirection =
            CameraUtils.changeCameraPointOfViewUpZ(theta, phi, distanceFromOrigin)
        direction = cameraPositionAndDirection.direction
        CameraUtils.lookAtCameraUpZ(
            scratch,
            FloatArray(16),
            FloatArray(16),
            aspect,
            cameraPositionAndDirection.position,
            cameraPositionAndDirection.direction
        )
        draw()
    }

    fun setScrollValue(DeltaX: Float, DeltaY: Float) {
        theta += DeltaY * rollAlignment
        phi += DeltaX * rollAlignment * direction
    }

    private fun draw() {
        currentRGB = SequenceRGBChangeUtils.changeColor(elapsedTime, currentRGB)
        particlesBox?.color =
            floatArrayOf(currentRGB.r / 255f, currentRGB.g / 255f, currentRGB.b / 255f, 1f)
        particlesBox?.draw(scratch)
    }

    private val upTimerTask = object : (TimerTask) -> Unit {
        override fun invoke(p1: TimerTask) {
            elapsedTime += 0.1f
            if (elapsedTime > period) {
                elapsedTime = 0f
            }
        }
    }
}