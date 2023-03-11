package com.komeyama.shader_study_android.ui.study10

import android.opengl.GLES20
import android.opengl.Matrix
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

class Study10Renderer : GLRendererBase() {

    private var cube: Cube? = null
    private val scratch = FloatArray(16)

    private var aspect = 0f

    private var cameraViewMatrix = FloatArray(16)
    private var cameraProjectionMatrix = FloatArray(16)

    private var theta = Math.PI / 2
    private var phai = Math.PI / 2
    private var rollAlignment = 0.01f
    private var direction = 1f
    private val distanceFromOrigin = 1.5f

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        super.onSurfaceCreated(p0, p1)
        cube = Cube()
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

        changeCameraPointOfView()
        draw()
    }

    fun setScrollValue(DeltaX: Float, DeltaY: Float) {
        theta += DeltaY * rollAlignment
        phai += DeltaX * rollAlignment * direction
    }

    private fun draw() {
        cube?.draw(scratch)
    }

    private fun changeCameraPointOfView() {
        if (0 <= sin(theta)) {
            direction = 1f
            val position = CameraPosition(
                x = (distanceFromOrigin * sin(theta) * cos(phai)).toFloat(),
                y = (distanceFromOrigin * sin(theta) * sin(phai)).toFloat(),
                z = (distanceFromOrigin * cos(theta)).toFloat()
            )
            lookAtCamera(position, direction)
        } else if (sin(theta) < 0) {
            direction = -1f
            val position = CameraPosition(
                x = (distanceFromOrigin * sin(-theta) * sin(-phai)).toFloat(),
                y = (distanceFromOrigin * sin(-theta) * cos(-phai)).toFloat(),
                z = (distanceFromOrigin * cos(-theta)).toFloat()
            )
            lookAtCamera(position, direction)
        }
        Matrix.frustumM(cameraProjectionMatrix, 0, -aspect, aspect, -1.0f, 1.0f, 1.0f, 100f)
        Matrix.multiplyMM(scratch, 0, cameraProjectionMatrix, 0, cameraViewMatrix, 0)
    }

    private fun lookAtCamera(position: CameraPosition, direction: Float) {
        Matrix.setLookAtM(
            cameraViewMatrix, 0,
            position.x, position.y, position.z,
            0.0f, 0.0f, 0.0f,
            0.0f, 0f, direction
        )
    }

    data class CameraPosition(val x: Float, val y: Float, val z: Float)
}