package com.komeyama.shader_study_android.ui.study8

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase

/**
 * ref. iOS app Woahdude (https://apps.apple.com/jp/app/woahdude/id1231780380)
 */
class ExpandingCircle(
    override val drawOrder: ShortArray = shortArrayOf(0, 1, 2, 0, 2, 3),
    override val vertexCoordinates: FloatArray = floatArrayOf(
        -1f, 1f, 0f,
        -1f, -1f, 0f,
        1f, -1f, 0f,
        1f, 1f, 0f
    ),
    override val initColor: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
) : ShaderBase() {

    var resolution = floatArrayOf(0.0f, 0.0f)
    var factor = 0.1f

    override fun vertexShaderCode(): String {
        return "" +
                "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"
    }

    override fun fragmentShaderCode(): String {
        return "" +
                "precision mediump float;" +
                "uniform float vTime;" +
                "uniform vec2 vResolution;" +
                "uniform float factor;" +
                "" +
                "void main() {" +
                "   vec2 st = (gl_FragCoord.xy * 2.0 - vResolution) / min(vResolution.x, vResolution.y);" +
                "   float f = 0.0;" +
                "   for (float i = 0.0; i < 50.0; i++) {" +
                "       float s = sin(vTime + i) * 0.5;" +
                "       float c = cos(vTime + i) * 0.5;" +
                "       f += 0.0025 / abs(length(st * 1.6 - vec2(c,s)) - abs(sin(vTime)));" +
                "   }" +
                "   gl_FragColor = vec4(vec3(1.0 * f * factor), 1.0);" +
                "}"
    }

    override fun handleFragmentParameter() {
        GLES20.glGetUniformLocation(program, "vResolution").also { resolutionHandle ->
            GLES20.glUniform2fv(resolutionHandle, 1, resolution, 0)
        }

        GLES20.glGetUniformLocation(program, "factor").also { factorHandle ->
            GLES20.glUniform1f(factorHandle, factor)
        }
    }
}