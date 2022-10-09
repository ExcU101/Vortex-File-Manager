package io.github.excu101.filesystem.fs.channel

import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.fs.listener.FileChannelListener
import java.io.Closeable

interface ReactiveFileChannel : Closeable {

    fun write(): ReactiveFileChannel

    fun read(): ReactiveFileChannel

    fun addReadBuffer(buffer: ByteBuffer): ReactiveFileChannel

    fun removeReadBuffer(buffer: ByteBuffer): ReactiveFileChannel

    fun addWriteBuffer(buffer: ByteBuffer): ReactiveFileChannel

    fun removeWriteBuffer(buffer: ByteBuffer): ReactiveFileChannel

    fun addListener(listener: FileChannelListener): ReactiveFileChannel

    fun removeListener(listener: FileChannelListener): ReactiveFileChannel

    override fun close()

}