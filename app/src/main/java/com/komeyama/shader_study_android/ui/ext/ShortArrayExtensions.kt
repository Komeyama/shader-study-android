package com.komeyama.shader_study_android.ui.ext

import com.komeyama.shader_study_android.ui.utils.ByteBufferUtils
import java.nio.ByteBuffer
import java.nio.ShortBuffer

fun ShortArray.allocateDirect(): ShortBuffer {
    return ByteBufferUtils.allocateDirectBuffer(this.size * 2, ByteBuffer::asShortBuffer) {
        (it as ShortBuffer).put(this)
    }
}