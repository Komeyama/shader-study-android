package com.komeyama.shader_study_android.ui.study3

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase

class LightSphere(
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
    var rgb = floatArrayOf(0.5f, 0.5f, 0.5f)

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
                "uniform vec2 vResolution;" +
                "uniform float vTime;" +
                "uniform vec3 vRGB;" +
                "const float pi = 3.1415927;" +
                "" +
                "vec2 rot(float rad){" +
                "   return vec2(sin(rad),cos(rad));" +
                "}" +
                "" +
                "void main(void){" +
                "   vec2 p = (gl_FragCoord.xy * 2.0 - vResolution) / min(vResolution.x, vResolution.y);" +
                "   float l = 0.1 / length(p);" +
                "   l += 0.1 / length(p + 0.5 * rot(vTime + 0.0));" +
                "   l += 0.1 / length(p + 0.5 * rot(vTime + pi / 2.0));" +
                "   l += 0.1 / length(p + 0.5 * rot(vTime + pi));" +
                "   l += 0.1 / length(p + 0.5 * rot(vTime + pi * (3.0/2.0)));" +
                "   gl_FragColor = vec4(l*vRGB, 1.0);" +
                "}"
    }

    override fun handleFragmentParameter() {
        GLES20.glGetUniformLocation(program, "vResolution").also { resolutionHandle ->
            GLES20.glUniform2fv(resolutionHandle, 1, resolution, 0)
        }
        GLES20.glGetUniformLocation(program, "vRGB").also { rgbHandle ->
            GLES20.glUniform3fv(rgbHandle, 1, rgb, 0)
        }
    }
}