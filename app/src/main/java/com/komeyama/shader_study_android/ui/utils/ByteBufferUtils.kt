package com.komeyama.shader_study_android.ui.utils

import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.reflect.KFunction1

object ByteBufferUtils {
    fun <K : Buffer> allocateDirectBuffer(
        capacity: Int,
        bufferFormat: KFunction1<ByteBuffer, K>,
        innerBlock: (buffer: Buffer) -> Unit
    ): K {
        return ByteBuffer.allocateDirect(capacity).run {
            order(ByteOrder.nativeOrder())
            bufferFormat.invoke(this).apply {
                innerBlock.invoke(this)
                position(0)
            }
        }
    }
}