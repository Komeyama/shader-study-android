package com.komeyama.shader_study_android.ui.study9

import android.opengl.GLES20
import com.komeyama.shader_study_android.ui.base.ShaderBase

/**
 * ref. iOS app Woahdude (https://apps.apple.com/jp/app/woahdude/id1231780380)
 */
class Galaxy(
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
                "" +
                "uniform float vTime;" +
                "uniform vec2 vResolution;" +
                "uniform float factor;" +
                "" +
                "const float pi = 3.1415926;" +
                "const int iterations = 10;" +
                "const int volsteps = 5;" +
                "const float stepsize = 0.290;" +
                "const float zoom = 0.900;" +
                "const float tile = 0.850;" +
                "const float brightness = 0.002;" +
                "const float distfading = 0.560;" +
                "const float saturation = 0.700;" +
                "const float transverse_speed = zoom * 0.1;" +
                "" +
                "void main() {" +
                "   vec2 uv = (gl_FragCoord.xy * 2.0 - vResolution) / min(vResolution.x, vResolution.y);" +
                "" +
                "   float time_viewing = vTime * factor;" +
                "   float speed = 0.01 * cos(time_viewing * 0.02 + pi/4.0);" +
                "   float formuparam = 0.79;" +
                "" +
                "   float a_xz = 0.9;" +
                "   float a_yz = -.6;" +
                "   float a_xy = 0.9 + vTime * 0.08;" +
                "   mat2 rot_xz = mat2(cos(a_xz),sin(a_xz),-sin(a_xz),cos(a_xz));" +
                "   mat2 rot_yz = mat2(cos(a_yz),sin(a_yz),-sin(a_yz),cos(a_yz));" +
                "   mat2 rot_xy = mat2(cos(a_xy),sin(a_xy),-sin(a_xy),cos(a_xy));" +
                "" +
                "   float v2 = 1.0;" +
                "   vec3 dir = vec3(uv * zoom, 1.);" +
                "   vec3 from = vec3(0.0, 0.0, 0.0);" +
                "   from.x -= 5.0 * (1.0 - 1.0);" +
                "   from.y -= 5.0 * (1.0 - 0.7);" +
                "" +
                "   vec3 forward = vec3(0.,0.,1.);" +
                "   from.x += transverse_speed * (11.0) * cos(0.01 * vTime) + 0.001 * vTime;" +
                "   from.y += transverse_speed * (-21.0) * sin(0.01 * vTime) + 0.001 * vTime;" +
                "   from.z += 0.003 * vTime;" +
                "" +
                "   dir.xy *= rot_xy;" +
                "   forward.xy *= rot_xy;" +
                "   dir.xz *= rot_xz;" +
                "   forward.xz *= rot_xz;" +
                "   dir.yz *= rot_yz;" +
                "   forward.yz *= rot_yz;" +
                "" +
                "   from.xy *= -rot_xy;" +
                "   from.xz *= rot_xz;" +
                "   from.yz *= rot_yz;" +
                "" +
                "   float zoom2 = (time_viewing - 22.) * speed;" +
                "   from += forward * zoom2;" +
                "   float sampleShift = mod(zoom2, stepsize);" +
                "" +
                "   float zoffset = - sampleShift;" +
                "   sampleShift /= stepsize;" +
                "" +
                "   float s = 0.24;" +
                "   float s3 = s + stepsize / 0.5;" +
                "   vec3 v = vec3(0.);" +
                "   float t3 = 0.0;" +
                "" +
                "   vec3 backCol2 = vec3(0.);" +
                "   for (int r=0; r < volsteps; r++) {" +
                "       vec3 p2 = from + (s + zoffset) * dir;" +
                "       vec3 p3 = from + (s3 + zoffset) * dir;" +
                "" +
                "       p2 = abs(vec3(tile) - mod(p2,vec3(tile * 2.)));" +
                "       p3 = abs(vec3(tile) - mod(p3,vec3(tile * 2.)));" +
                "" +
                "       float pa, a = pa = 0.;" +
                "       for (int i=0; i< iterations; i++) {" +
                "           p2 = abs(p2) / dot(p2,p2) - formuparam;" +
                "           float D = abs(length(p2) - pa);" +
                "           a += i > 7 ? min(12., D) : D;" +
                "           pa = length(p2);" +
                "       }" +
                "" +
                "       a *= a * a;" +
                "       float s1 = s + zoffset;" +
                "       float fade = pow(distfading, max(0., float(r) - sampleShift));" +
                "       v += fade;" +
                "" +
                "       if(r == 0) {" +
                "           fade *= (1. - sampleShift);" +
                "       }" +
                "       if(r == volsteps - 1) {" +
                "           fade *= sampleShift;" +
                "       }" +
                "       v += vec3(s1,s1*s1,s1*s1*s1*s1) * a * brightness * fade;" +
                "       backCol2 += mix(.4, 1., v2) * vec3(1.8 * t3 * t3 * t3, 1.4 * t3 * t3, t3) * fade;" +
                "       s += stepsize;" +
                "       s3 += stepsize;" +
                "       " +
                "   }" +
                "" +
                "   v = mix(vec3(length(v)),v,saturation);" +
                "" +
                "   vec4 forCol2 = vec4(v * .01, 1.);" +
                "   backCol2.b *= -3.8;" +
                "   backCol2.r *= 3.21;" +
                "" +
                "   backCol2.b = 10.5 * mix(backCol2.g, backCol2.b, 0.01);" +
                "   backCol2.g = 0.1;" +
                "   backCol2.bg = mix(backCol2.gb, backCol2.bg, 0.39 * (cos(1.00) + 1.0));" +
                "   gl_FragColor = forCol2 + vec4(backCol2, 1.0);" +
                "}" +
                ""
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