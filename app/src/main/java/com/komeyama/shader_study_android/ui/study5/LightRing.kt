package com.komeyama.shader_study_android.ui.study5

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase

class LightRing(
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
                "void main(void){" +
                "   vec2 p = (gl_FragCoord.xy * 2.0 - vResolution) / min(vResolution.x, vResolution.y);" +
                "   float t = 0.02 / abs(0.5 - length(p));" +
                "   gl_FragColor = vec4(vec3(t), 1.0);" +
                "}"
    }

    override fun handleFragmentParameter() {
        GLES20.glGetUniformLocation(program, "vResolution").also { resolutionHandle ->
            GLES20.glUniform2fv(resolutionHandle, 1, resolution, 0)
        }
    }

}