package com.komeyama.shader_study_android.ui.utils

import android.graphics.Point
import android.graphics.PointF
import kotlin.math.abs

object RendererUtils {
    fun getLinePoints(p0: PointF, p1: PointF): List<PointF> {
        val points = mutableListOf<Point>()
        val pX0 = (p0.x * 1f).toInt()
        val pY0 = (p0.y * 1f).toInt()
        val pX1 = (p1.x * 1f).toInt()
        val pY1 = (p1.y * 1f).toInt()

        var x0: Int = pX0
        var y0: Int = pY0
        val x1: Int = pX1
        val y1: Int = pY1
        val dx: Int = (abs(pX1 - pX0))
        val dy: Int = (abs(pY1 - pY0))
        val sx: Int = if (pX1 > pX0) 1 else -1
        val sy: Int = if (pY1 > pY0) 1 else -1
        var err = dx - dy
        while (true) {
            if (x0 >= 0 && y0 >= 0) {
                points.add(Point(x0, y0))
            }
            if (x0 == x1 && y0 == y1) {
                break
            }
            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x0 += sx
            }
            if (e2 < dx) {
                err += dx
                y0 += sy
            }
        }
        return points.map {
            PointF(
                it.x * 1f,
                it.y * 1f
            )
        }
    }
}