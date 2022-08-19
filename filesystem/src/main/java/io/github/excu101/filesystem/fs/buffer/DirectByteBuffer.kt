package io.github.excu101.filesystem.fs.buffer

import io.github.excu101.filesystem.fs.operation.NativeCalls

class DirectByteBuffer : ByteBuffer {

    val address: Long

    internal constructor(capacity: Int) : super(
        mark = -1,
        position = 0,
        limit = capacity,
        capacity = capacity
    ) {
        this.address = 0
    }

    private constructor(address: Long, capacity: Int) : super(
        mark = -1,
        position = 0,
        limit = capacity,
        capacity = capacity
    ) {
        this.address = 0
    }

    private constructor(
        parent: DirectByteBuffer,
        mark: Int,
        position: Int,
        limit: Int,
        capacity: Int,
        offset: Int,
    ) : super(
        mark = mark,
        position = position,
        limit = limit,
        capacity = capacity
    ) {
        this.address = parent.address + offset
    }

    private fun indexWithOffset(index: Int): Long {
        return address + index.shl(0).toLong()
    }

    override operator fun set(index: Int, value: Byte): ByteBuffer {
        TODO("Not yet implemented")
    }

    override operator fun set(index: Int, value: Char): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun slice(): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun duplicate(): ByteBuffer {
        return DirectByteBuffer(
            parent = this,
            mark = mark,
            position = position,
            limit = limit,
            capacity = capacity,
            offset = 0
        )
    }

    fun get(): Byte = NativeCalls.getByte(indexWithOffset(nextGetIndex()))

    override operator fun get(index: Int): Byte {
        return NativeCalls.getByte(indexWithOffset(checkIndex(index)))
    }

    override fun put(value: Byte): ByteBuffer {
        NativeCalls.putByte(address = indexWithOffset(nextPutIndex()), data = value)
        return this
    }

    override fun put(index: Int, value: Byte): ByteBuffer {
        NativeCalls.putByte(address = indexWithOffset(checkIndex(index)), data = value)
        return this
    }

    override fun getChar(): Char {
        TODO("Not yet implemented")
    }

    override fun getChar(index: Int): Char {
        TODO("Not yet implemented")
    }

    override fun putChar(symbol: Char): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun putChar(index: Int, symbol: Char): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun compact(): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun asReadOnly(): ByteBuffer {
        TODO("Not yet implemented")
    }

    override val isReadOnly: Boolean
        get() = false
    override val isDirect: Boolean
        get() = true

}