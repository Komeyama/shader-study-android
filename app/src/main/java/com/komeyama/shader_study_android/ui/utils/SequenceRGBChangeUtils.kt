package com.komeyama.shader_study_android.ui.utils

import com.komeyama.shader_study_android.ui.utils.dto.RGB

object SequenceRGBChangeUtils {

    const val period = 6000f

    fun changeColor(currentTime: Float, currentRGB: RGB): RGB {
        when (currentTime) {
            in 0f..1000f -> {
                val g = (255f * currentTime / 1000f).toLong()
                return currentRGB.copy(g = g)
            }
            in 1001f..2000f -> {
                val r = (255f * (1f - (currentTime / 2000f))).toLong()
                return currentRGB.copy(r = r)
            }
            in 2001f..3000f -> {
                val b = (255f * (currentTime / 3000f)).toLong()
                return currentRGB.copy(b = b)
            }
            in 3001f..4000f -> {
                val g = (255f * (1f - (currentTime / 4000f))).toLong()
                return currentRGB.copy(g = g)
            }
            in 4001f..5000f -> {
                val r = (255f * (currentTime / 5000f)).toLong()
                return currentRGB.copy(r = r)
            }
            in 5001f..period -> {
                val b = (255f * (1f - (currentTime / 6000f))).toLong()
                return currentRGB.copy(b = b)
            }
            else -> {
                return currentRGB
            }
        }
    }
}