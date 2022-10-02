package io.github.excu101.filesystem.unix.channel

import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.channel.UnixBufferCalls.get
import io.github.excu101.filesystem.unix.channel.UnixBufferCalls.put

class UnixByteBuffer : ByteBuffer {

    constructor(capacity: Long) : super(
        mark = 0,
        position = 0,
        limit = capacity.toInt(),
        capacity = capacity.toInt(),
        offset = 0
    ) {
        _isReadOnly = false
        address = UnixCalls.allocate(capacity)
    }

    constructor(
        parent: UnixByteBuffer,
        isReadOnly: Boolean,
    ) : super(
        mark = parent.mark,
        position = parent.position,
        limit = parent.limit,
        capacity = parent.capacity,
        offset = parent.arrayOffset
    ) {
        _isReadOnly = isReadOnly
        this.address = parent.address
    }

    private val _isReadOnly: Boolean

    internal val address: Long

    private fun ix(index: Int): Long {
        return address + index
    }

    override fun slice(): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun duplicate(): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun get(): Byte {
        return get(address = ix(nextGetIndex()))
    }

    override fun get(index: Int): Byte {
        return get(address = ix(checkIndex(index)))
    }

    override fun put(value: Byte): ByteBuffer {
        put(address = ix(nextPutIndex()), value)
        return this
    }

    override fun put(index: Int, value: Byte): ByteBuffer {
        require(!isReadOnly) { "Read only buffer" }
        put(address = ix(checkIndex(index)), value)
        return this
    }

    override fun getChar(): Char {
        TODO("Not yet implemented")
    }

    override fun getChar(index: Int): Char {
        TODO("Not yet implemented")
    }

    override fun put(symbol: Char): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun put(index: Int, symbol: Char): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun compact(): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun asReadOnly(): ByteBuffer = UnixByteBuffer(
        parent = this,
        isReadOnly = isReadOnly
    )

    override val isReadOnly: Boolean
        get() = _isReadOnly

    override val isDirect: Boolean
        get() = true


}