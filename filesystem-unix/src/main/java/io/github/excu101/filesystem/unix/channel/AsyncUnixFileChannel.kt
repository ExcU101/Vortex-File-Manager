package io.github.excu101.filesystem.unix.channel

import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.fs.channel.AsyncFileChannel
import io.github.excu101.filesystem.fs.listener.FileChannelListener
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.calls.UnixFileChannelCalls
import io.github.excu101.filesystem.unix.calls.UnixFileChannelCalls.read
import java.io.FileDescriptor

class AsyncUnixFileChannel(
    descriptor: FileDescriptor,
) : AsyncFileChannel {

    private var isClosed = false

    private val readBuffers = mutableListOf<ByteBuffer>()
    private val writeBuffers = mutableListOf<ByteBuffer>()
    private val listeners = mutableListOf<FileChannelListener>()

    private val index = UnixCalls.getIndexDescriptor(descriptor)

    override fun read(): AsyncUnixFileChannel {
        if (isClosed) return this
        if (readBuffers.isEmpty()) return this

        var readBytes = -1

        if (readBuffers.size == 1) {
            val buffer = readBuffers[0]
            if (buffer.isDirect) {
                readBytes = read(
                    descriptor = index,
                    address = (buffer as UnixByteBuffer).address,
                    count = buffer.remaining
                )
            }
        } else {
            val addresses = readBuffers.map { (it as UnixByteBuffer).address }.toLongArray()

            readBytes = read(
                descriptor = index,
                addresses = addresses,
                count = readBuffers.maxBy {
                    it.remaining
                }.remaining
            )
        }

        onRead(readBytes)

        return this
    }

    override fun addReadBuffer(buffer: ByteBuffer): AsyncFileChannel {
        readBuffers.add(buffer)
        return this
    }

    override fun removeReadBuffer(buffer: ByteBuffer): AsyncFileChannel {
        readBuffers.remove(buffer)
        return this
    }

    override fun addWriteBuffer(buffer: ByteBuffer): AsyncFileChannel {
        writeBuffers.add(buffer)
        return this
    }

    override fun removeWriteBuffer(buffer: ByteBuffer): AsyncFileChannel {
        writeBuffers.remove(buffer)
        return this
    }

    override fun addListener(listener: FileChannelListener): AsyncFileChannel {
        listeners.add(listener)
        return this
    }

    override fun removeListener(listener: FileChannelListener): AsyncFileChannel {
        listeners.remove(listener)
        return this
    }

    override fun write(): AsyncUnixFileChannel {
        if (isClosed) return this
        if (writeBuffers.isEmpty()) return this
        var writeBytes = -1

        if (writeBuffers.size == 1) {
            val buffer = writeBuffers.first()
            if (buffer.isDirect) {
                writeBytes = UnixFileChannelCalls.write(
                    descriptor = index,
                    address = (buffer as UnixByteBuffer).address,
                    count = buffer.remaining
                )
            }
        } else {
            writeBytes = UnixFileChannelCalls.write(
                descriptor = index,
                addresses = writeBuffers.map {
                    (it as UnixByteBuffer).address
                }.toLongArray(),
                count = writeBuffers.maxOf { it.remaining }
            )
        }

        onWrite(writeBytes)

        return this
    }

    override fun close() {
        if (isClosed) return

        if (UnixCalls.close(descriptor = index)) {
            onClose()
            isClosed = true
        }
    }

    private fun onRead(bytes: Int) {
        listeners.forEach { listener ->
            listener.onRead(bytes)
        }
    }

    private fun onWrite(bytes: Int) {
        listeners.forEach { listener ->
            listener.onWrite(bytes)
        }
    }

    private fun onClose() {
        listeners.forEach { listener ->
            listener.onClose()
        }
    }

}