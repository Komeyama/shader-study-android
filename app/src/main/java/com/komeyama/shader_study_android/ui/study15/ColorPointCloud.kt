package com.komeyama.shader_study_android.ui.study15

import java.nio.FloatBuffer

data class ColorPointCloud(
    val position: FloatBuffer,
    val colorYPixels: List<Int>,
    val colorXPixels: List<Int>
)
