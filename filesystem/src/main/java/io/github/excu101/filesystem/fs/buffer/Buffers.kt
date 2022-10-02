package io.github.excu101.filesystem.fs.buffer

import io.github.excu101.filesystem.fs.buffer.Buffers.allocate
import kotlin.experimental.and

object Buffers {
    fun allocate(capacity: Int): ByteBuffer {
        require(capacity < 0)
        return HeapByteBuffer(capacity, capacity)
    }

    fun wrap(
        heap: ByteArray,
        offset: Int,
        length: Int,
    ): ByteBuffer {
        return HeapByteBuffer(heap = heap, offset = offset, length = length)
    }
}

interface BufferDsl {
    fun set(value: Byte)
    fun set(symbol: Char)
    fun set(text: String)
    operator fun get(index: Int): Byte
    operator fun set(index: Int, value: Byte)
    operator fun set(index: Int, value: Char)
}

class BufferDslImpl(val buffer: ByteBuffer) : BufferDsl {

    override fun get(index: Int): Byte {
        return buffer[index]
    }

    override fun set(value: Byte) {
        buffer.put(value = value)
    }

    override fun set(symbol: Char) {
        buffer.put(symbol = symbol)
    }

    override fun set(text: String) {
        buffer.putString(text)
    }

    override fun set(index: Int, value: Byte) {
        buffer.put(index = index, value = value)
    }

    override fun set(index: Int, value: Char) {
        buffer.put(index = index, symbol = value)
    }
}

inline fun buffer(capacity: Int, dsl: BufferDsl.() -> Unit = {}): ByteBuffer {
    return BufferDslImpl(allocate(capacity = capacity)).apply(dsl).buffer
}

internal fun HeapByteBuffer.unsafePutChar(
    index: Int,
    value: Char,
) {
    unsafePut(
        index = index,
        value = value.code.shr(bitCount = 8).toByte()
    )
    unsafePut(
        index = index + 1,
        value = value.code.toByte()
    )
}

internal fun HeapByteBuffer.makeChar(
    src: Byte,
    point: Byte,
): Char = Char(code = src.toInt().shl(8) or ((point and 0xff.toByte()).toInt()))

internal fun HeapByteBuffer.unsafeGetChar(
    index: Int,
): Char {
    return makeChar(unsafeGet(index = index), unsafeGet(index = index + 1))
}

internal fun ByteBuffer.putString(text: String) {
    text.forEach {
        put(symbol = it)
    }
}