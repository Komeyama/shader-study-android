package com.komeyama.shader_study_android.ui.utils

import android.opengl.Matrix
import com.komeyama.shader_study_android.ui.utils.dto.Position
import kotlin.math.cos
import kotlin.math.sin

object CameraUtils {

    class CameraPositionAndDirection(
        val position: Position,
        val direction: Float
    )

    fun changeCameraPointOfViewUpY(
        theta: Double,
        phi: Double,
        distanceFromOrigin: Float
    ): CameraPositionAndDirection {
        var direction = 1f
        var position = Position()
        if (0 <= sin(theta)) {
            position = Position(
                x = (distanceFromOrigin * sin(theta) * sin(phi)).toFloat(),
                y = (distanceFromOrigin * cos(theta)).toFloat(),
                z = (distanceFromOrigin * sin(theta) * cos(phi)).toFloat()
            )
        } else if (sin(theta) < 0) {
            direction = -1f
            position = Position(
                x = (distanceFromOrigin * sin(-theta) * cos(-phi - Math.PI / 2)).toFloat(),
                y = (distanceFromOrigin * cos(-theta)).toFloat(),
                z = (distanceFromOrigin * sin(-theta) * sin(-phi - Math.PI / 2)).toFloat(),
            )
        }
        return CameraPositionAndDirection(position, direction)
    }

    fun lookAtCameraUpY(
        scratch: FloatArray,
        cameraProjectionMatrix: FloatArray,
        cameraViewMatrix: FloatArray,
        aspect: Float,
        position: Position,
        direction: Float
    ) {
        Matrix.setLookAtM(
            cameraViewMatrix, 0,
            position.x, position.y, position.z,
            0.0f, 0.0f, 0.0f,
            0.0f, direction, 0f
        )
        Matrix.frustumM(cameraProjectionMatrix, 0, -aspect, aspect, -1.0f, 1.0f, 1.0f, 100f)
        Matrix.multiplyMM(scratch, 0, cameraProjectionMatrix, 0, cameraViewMatrix, 0)
    }

    fun changeCameraPointOfView(
        theta: Double,
        phi: Double,
        distanceFromOrigin: Float
    ): CameraPositionAndDirection {
        var direction = 1f
        var position = Position()
        if (0 <= sin(theta)) {
            position = Position(
                x = (distanceFromOrigin * sin(theta) * cos(phi)).toFloat(),
                y = (distanceFromOrigin * sin(theta) * sin(phi)).toFloat(),
                z = (distanceFromOrigin * cos(theta)).toFloat()
            )
        } else if (sin(theta) < 0) {
            direction = -1f
            position = Position(
                x = (distanceFromOrigin * sin(-theta) * sin(-phi)).toFloat(),
                y = (distanceFromOrigin * sin(-theta) * cos(-phi)).toFloat(),
                z = (distanceFromOrigin * cos(-theta)).toFloat()
            )
        }
        return CameraPositionAndDirection(position, direction)
    }

    fun lookAtCamera(
        scratch: FloatArray,
        cameraProjectionMatrix: FloatArray,
        cameraViewMatrix: FloatArray,
        aspect: Float,
        position: Position,
        direction: Float
    ) {
        Matrix.setLookAtM(
            cameraViewMatrix, 0,
            position.x, position.y, position.z,
            0.0f, 0.0f, 0.0f,
            0.0f, 0f, direction
        )
        Matrix.frustumM(cameraProjectionMatrix, 0, -aspect, aspect, -1.0f, 1.0f, 1.0f, 100f)
        Matrix.multiplyMM(scratch, 0, cameraProjectionMatrix, 0, cameraViewMatrix, 0)
    }
}