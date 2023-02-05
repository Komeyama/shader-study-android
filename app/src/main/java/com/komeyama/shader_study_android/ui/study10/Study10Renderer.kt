package com.komeyama.shader_study_android.ui.study10

import android.opengl.GLES20
import android.opengl.Matrix
import android.os.SystemClock
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Study10Renderer : GLRendererBase() {

    private var cube: Cube? = null
    private val scratch = FloatArray(16)
    private var angle = 1f
    private var time = 0f
    private var startTime = SystemClock.uptimeMillis()
    private val mPMatrix = FloatArray(16)
    private val mVMatrix = FloatArray(16)

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        super.onSurfaceCreated(p0, p1)
        cube = Cube()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        GLES20.glViewport(0, 0, width, height)
        val aspect: Float = width.toFloat() / height
        Matrix.setLookAtM(
            mVMatrix, 0,
            -0.0f, -0.0f, -2.0f,
            0f, 0f, 0f,
            0f, 1.0f, 0.0f
        )
        Matrix.frustumM(mPMatrix, 0, -aspect, aspect, -1.0f, 1.0f, 1.0f, 100f)
        Matrix.multiplyMM(scratch, 0, mPMatrix, 0, mVMatrix, 0)
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        p0?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        draw()
    }

    private fun draw() {
        cube?.mvpMatrix = scratch
        cube?.draw(scratch)
        time = (SystemClock.uptimeMillis().toFloat() - startTime) / 1000
        Matrix.rotateM(cube?.mvpMatrix, 0, abs(sin(time * 2)), sin(time), cos(time), tan(time / 2))
    }
}