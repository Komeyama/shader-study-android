package com.komeyama.shader_study_android.ui.study16

object LinearInterpolationShaderCodes {
    const val VERTICAL = "precision mediump float;" +
            "uniform vec2 vResolution;" +
            "void main() {" +
            "   vec2 pos = gl_FragCoord.xy / vResolution.xy;" +
            "   vec3 left = vec3(1.0, 0.53, 0.0);" +
            "   vec3 right = vec3(0.14, 0.55, 1.0);" +
            "   vec3 col = mix(left, right, pos.y);" +
            "   gl_FragColor = vec4(col, 1.0);" +
            "}"

    const val CIRCLE = "" +
            "precision mediump float;" +
            "uniform vec2 vResolution;" +
            "void main() {" +
            "   vec2 pos = (gl_FragCoord.xy * 2.0 - vResolution) / min(vResolution.x, vResolution.y);" +
            "   vec3 center = vec3(1.0, 0.53, 0.0);" +
            "   vec3 outside = vec3(0.14, 0.55, 1.0);" +
            "   vec3 col = mix(center, outside, sqrt(pos.x * pos.x + pos.y * pos.y));" +
            "   gl_FragColor = vec4(col, 1.0);" +
            "}"
}
