package io.github.excu101.filesystem.unix.calls

internal object UnixBufferCalls {

    init {
        System.loadLibrary("unix-buffer")
    }

    internal fun put(address: Long, byte: Byte) = putImpl(address, byte)

    private external fun putImpl(address: Long, byte: Byte)

    internal fun get(address: Long) = getImpl(address)

    private external fun getImpl(address: Long): Byte

}