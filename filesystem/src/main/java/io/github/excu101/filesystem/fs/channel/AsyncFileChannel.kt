package io.github.excu101.filesystem.fs.channel

import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.fs.listener.FileChannelListener
import java.io.Closeable

interface AsyncFileChannel : Closeable {

    fun write(): AsyncFileChannel

    fun read(): AsyncFileChannel

    fun addReadBuffer(buffer: ByteBuffer): AsyncFileChannel

    fun removeReadBuffer(buffer: ByteBuffer): AsyncFileChannel

    fun addWriteBuffer(buffer: ByteBuffer): AsyncFileChannel

    fun removeWriteBuffer(buffer: ByteBuffer): AsyncFileChannel

    fun addListener(listener: FileChannelListener): AsyncFileChannel

    fun removeListener(listener: FileChannelListener): AsyncFileChannel

    override fun close()

}