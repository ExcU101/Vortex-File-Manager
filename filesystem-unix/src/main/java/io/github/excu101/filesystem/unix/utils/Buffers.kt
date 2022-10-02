package io.github.excu101.filesystem.unix.utils

import io.github.excu101.filesystem.fs.buffer.Buffers
import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.unix.channel.UnixByteBuffer

@Suppress(names = ["FunctionName"])
fun Buffers.UnixDirect(
    capacity: Long,
): ByteBuffer {
    return UnixByteBuffer(capacity)
}