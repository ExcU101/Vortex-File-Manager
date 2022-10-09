package io.github.excu101.filesystem.unix.channel

import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.fs.channel.ReactiveFileChannel
import io.github.excu101.filesystem.fs.listener.FileChannelListener
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.channel.UnixFileChannelCalls.read
import java.io.FileDescriptor

class ReactiveUnixFileChannel(
    descriptor: FileDescriptor,
) : ReactiveFileChannel {

    private var isClosed = false

    private val readBuffers = mutableListOf<ByteBuffer>()
    private val writeBuffers = mutableListOf<ByteBuffer>()
    private val listeners = mutableListOf<FileChannelListener>()

    private val index = UnixCalls.getIndexDescriptor(descriptor)

    override fun read(): ReactiveUnixFileChannel {
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
            readBytes = read(
                descriptor = index,
                addresses = readBuffers.map {
                    (it as UnixByteBuffer).address
                }.toLongArray(),
                count = readBuffers.maxBy {
                    it.remaining
                }.remaining
            )
        }

        onRead(readBytes)

        return this
    }

    override fun addReadBuffer(buffer: ByteBuffer): ReactiveFileChannel {
        readBuffers.add(buffer)
        return this
    }

    override fun removeReadBuffer(buffer: ByteBuffer): ReactiveFileChannel {
        readBuffers.remove(buffer)
        return this
    }

    override fun addWriteBuffer(buffer: ByteBuffer): ReactiveFileChannel {
        writeBuffers.add(buffer)
        return this
    }

    override fun removeWriteBuffer(buffer: ByteBuffer): ReactiveFileChannel {
        writeBuffers.remove(buffer)
        return this
    }

    override fun addListener(listener: FileChannelListener): ReactiveFileChannel {
        listeners.add(listener)
        return this
    }

    override fun removeListener(listener: FileChannelListener): ReactiveFileChannel {
        listeners.remove(listener)
        return this
    }

    override fun write(): ReactiveUnixFileChannel {
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