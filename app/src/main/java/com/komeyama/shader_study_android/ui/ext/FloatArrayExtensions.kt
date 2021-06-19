package com.komeyama.shader_study_android.ui.ext

import com.komeyama.shader_study_android.ui.utils.ByteBufferUtils
import java.nio.ByteBuffer
import java.nio.FloatBuffer

fun FloatArray.allocateDirect(): FloatBuffer {
    return ByteBufferUtils.allocateDirectBuffer(this.size * 4, ByteBuffer::asFloatBuffer) {
        (it as FloatBuffer).put(this)
    }
}