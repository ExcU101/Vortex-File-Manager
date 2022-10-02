package io.github.excu101.filesystem.fs.channel

import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import java.io.Closeable

interface ReactiveFileChannel : Closeable {

    fun write(): ReactiveFileChannel

    fun read(): ReactiveFileChannel

    fun addBuffer(buffer: ByteBuffer): ReactiveFileChannel

    fun removeBuffer(buffer: ByteBuffer): ReactiveFileChannel

    override fun close()

}