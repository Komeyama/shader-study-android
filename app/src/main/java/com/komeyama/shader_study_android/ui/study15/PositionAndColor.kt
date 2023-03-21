package com.komeyama.shader_study_android.ui.study15

import java.nio.FloatBuffer

data class PositionAndColor(
    val positionBuffer: FloatBuffer,
    val colorBuffer: FloatBuffer,
    val size: Int,
)
