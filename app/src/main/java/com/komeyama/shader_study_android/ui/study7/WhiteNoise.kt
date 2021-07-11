package com.komeyama.shader_study_android.ui.study7

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase

class WhiteNoise(
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
    var factor = 1.0f

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
                "float random(vec2 st) {" +
                "   return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);" +
                "}" +
                "" +
                "void main() {" +
                "   vec2 st = (gl_FragCoord.xy * 2.0 - vResolution) / min(vResolution.x, vResolution.y);" +
                "   float rnd = random(floor(st * factor) + sin(vTime));" +
                "   gl_FragColor = vec4(vec3(rnd),1.0);" +
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