package com.komeyama.shader_study_android.ui.base

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class RenderBase : GLSurfaceView.Renderer {

    protected val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        cameraViewConversion()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        homography(width, height)
    }

    private fun cameraViewConversion() {
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    private fun homography(width: Int, height: Int) {
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }
}


/*
class MyGLRenderer : GLSurfaceView.Renderer {

    private var mTriangle: Triangle? = null

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private val rotationMatrix = FloatArray(16)

    @Volatile
    var angle: Float = 0f

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        // initialize a triangle
        mTriangle = Triangle()
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        cameraViewConversion()
        rotateAndDraw()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        homography(width, height)
    }

    private fun homography(width: Int, height: Int) {
        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    fun changeTriangleColor(r: Float, g: Float, b: Float, a: Float) {
        mTriangle?.color = floatArrayOf(r / 255.0f, g / 255.0f, b / 255.0f, a)
    }

    private fun cameraViewConversion() {
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    private fun rotateAndDraw() {
        val scratch = FloatArray(16)
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
        mTriangle?.draw(scratch)
    }
}
*/