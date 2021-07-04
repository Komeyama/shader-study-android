package com.komeyama.shader_study_android.ui.study6

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase

/**
 * ref. https://wgld.org/d/glsl/g005.html
 */
class Mandelbrot (
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
    var dragPosition = floatArrayOf(0.5f, 0.5f)

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
                "uniform vec2 vDrag;" +
                "" +
                "vec3 hsv(float h, float s, float v){" +
                "    vec4 t = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);" +
                "    vec3 p = abs(fract(vec3(h) + t.xyz) * 6.0 - vec3(t.w));" +
                "    return v * mix(vec3(t.x), clamp(p - vec3(t.x), 0.0, 1.0), s);" +
                "}" +
                "" +
                "void main(void){" +
                "    vec2 p = (gl_FragCoord.xy * 2.0 - vResolution) / min(vResolution.x, vResolution.y);" +
                "    int j = 0;" +
                "    vec2  x = p + vec2(-0.5, 0.0);" +
                "    float y = 1.5 - vDrag.x * 0.5;" +
                "    vec2  z = vec2(0.0, 0.0);" +
                "" +
                "    for(int i = 0; i < 360; i++){" +
                "        j++;" +
                "        if(length(z) > 2.0){break;}" +
                "        z = vec2(z.x * z.x - z.y * z.y, 2.0 * z.x * z.y) + x * y;" +
                "    }" +
                "" +
                "    float h = mod(vTime * 20.0, 360.0) / 360.0;" +
                "    vec3 rgb = hsv(h, 1.0, 1.0);" +
                "" +
                "    float t = float(j) / 360.0;" +
                "    gl_FragColor = vec4(rgb * t, 1.0);" +
                "}"
    }

    override fun handleFragmentParameter() {
        GLES20.glGetUniformLocation(program, "vResolution").also { resolutionHandle ->
            GLES20.glUniform2fv(resolutionHandle, 1, resolution, 0)
        }

        GLES20.glGetUniformLocation(program, "vDrag").also { dragHandle ->
            GLES20.glUniform2fv(dragHandle, 1, dragPosition, 0)
        }
    }

}