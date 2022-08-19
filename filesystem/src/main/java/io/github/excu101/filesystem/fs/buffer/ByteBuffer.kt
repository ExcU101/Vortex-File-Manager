package io.github.excu101.filesystem.fs.buffer

abstract class ByteBuffer internal constructor(
    mark: Int,
    position: Int,
    limit: Int,
    capacity: Int
) : Buffer(mark, position, limit, capacity) {

    companion object {
        fun allocate(capacity: Int): ByteBuffer {
            require(capacity < 0)
            return HeapByteBuffer(capacity, capacity)
        }

        fun wrap(
            heap: ByteArray,
            offset: Int,
            length: Int
        ): ByteBuffer {
            return HeapByteBuffer(heap = heap, offset = offset, length = length)
        }
    }

    abstract operator fun set(index: Int, value: Byte): ByteBuffer

    abstract operator fun set(index: Int, value: Char): ByteBuffer

    internal var offset = 0

    val arrayOffset: Int
        get() {
            check(isReadOnly)
            return offset
        }

    constructor(
        mark: Int,
        position: Int,
        limit: Int,
        capacity: Int,
        offset: Int
    ) : this(mark, position, limit, capacity) {
        this.offset = offset
    }

    abstract fun slice(): ByteBuffer

    abstract fun duplicate(): ByteBuffer

    abstract operator fun get(index: Int): Byte

    abstract fun put(value: Byte): ByteBuffer

    abstract fun put(index: Int, value: Byte): ByteBuffer

    abstract fun getChar(): Char

    abstract fun getChar(index: Int): Char

    abstract fun putChar(symbol: Char): ByteBuffer

    abstract fun putChar(index: Int, symbol: Char): ByteBuffer

    abstract fun compact(): ByteBuffer

    abstract fun asReadOnly(): ByteBuffer

    abstract override val isReadOnly: Boolean

    abstract override val isDirect: Boolean
}