package com.komeyama.shader_study_android.ui.ext

import com.komeyama.shader_study_android.ui.utils.ByteBufferUtils
import java.nio.ByteBuffer
import java.nio.IntBuffer

fun Int.allocateDirect(): IntBuffer {
    return ByteBufferUtils.allocateDirectBuffer(this * 4, ByteBuffer::asIntBuffer) {
        (it as IntBuffer).put(this)
    }
}