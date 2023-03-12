package com.komeyama.shader_study_android.ui.study14

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import com.komeyama.shader_study_android.R
import com.komeyama.shader_study_android.ui.base.GLRendererBase
import com.komeyama.shader_study_android.ui.utils.CameraUtils
import javax.microedition.khronos.opengles.GL10

class Study14Renderer(val context: Context) : GLRendererBase() {

    private var particlesBox: ParticlesPicture? = null
    private val scratch = FloatArray(16)

    private var aspect = 0f

    private var theta = Math.PI / 2
    private var phi = 0.001
    private var direction = 1f
    private var rollAlignment = 0.01f
    private val distanceFromOrigin = 1.5f

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(unused, width, height)
        GLES20.glViewport(0, 0, width, height)
        aspect = width.toFloat() / height
        particlesBox = ParticlesPicture(bitmap = createBitmap())
    }

    override fun onDrawFrame(p0: GL10?) {
        super.onDrawFrame(p0)
        p0?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        draw()
    }

    fun setScrollValue(DeltaX: Float, DeltaY: Float) {
        theta += DeltaY * rollAlignment
        phi += DeltaX * rollAlignment * direction
    }

    private fun draw() {
        val cameraPositionAndDirection =
            CameraUtils.changeCameraPointOfViewUpY(theta, phi, distanceFromOrigin)
        direction = cameraPositionAndDirection.direction
        CameraUtils.lookAtCameraUpY(
            scratch,
            FloatArray(16),
            FloatArray(16),
            aspect,
            cameraPositionAndDirection.position,
            cameraPositionAndDirection.direction
        )

        particlesBox?.draw(scratch)
    }

    private fun createBitmap(): Bitmap {
        val options = BitmapFactory.Options()
        return BitmapFactory.decodeResource(context.resources, R.drawable.sample_image, options)
    }
}