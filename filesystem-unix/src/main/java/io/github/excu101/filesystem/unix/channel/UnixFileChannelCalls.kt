package io.github.excu101.filesystem.unix.channel

internal object UnixFileChannelCalls {

    init {
        System.loadLibrary("unix-calls")
    }

    internal fun read(descriptor: Int, address: Long, count: Int) =
        readImpl(descriptor, address, count)

    private external fun readImpl(descriptor: Int, address: Long, count: Int): Int

    internal fun write(descriptor: Int, address: Long, count: Int) =
        writeImpl(descriptor, address, count)

    private external fun writeImpl(descriptor: Int, address: Long, count: Int): Int

    internal fun read(descriptor: Int, addresses: LongArray, count: Int) =
        readImpl(descriptor, addresses, count)

    private external fun readImpl(descriptor: Int, addresses: LongArray, count: Int): Int

    internal fun write(descriptor: Int, addresses: LongArray, count: Int) =
        writeImpl(descriptor, addresses, count)

    private external fun writeImpl(descriptor: Int, addresses: LongArray, count: Int): Int

}