package io.github.excu101.filesystem.unix.channel

import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.fs.channel.ReactiveFileChannel
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.listener.UnixFileChannelListener
import java.io.FileDescriptor

class ReactiveUnixFileChannel(
    private val descriptor: FileDescriptor,
    private val listeners: List<UnixFileChannelListener> = listOf(),
) : ReactiveFileChannel {

    private val buffers = mutableListOf<ByteBuffer>()

    private val index = UnixCalls.getIndexDescriptor(descriptor)

    override fun read(): ReactiveUnixFileChannel {
        if (buffers.isEmpty()) return this

        var readBytes = -1;

        if (buffers.size == 1) {
            val buffer = buffers[0]
            if (buffer.isDirect) {
                readBytes = UnixFileChannelCalls.read(
                    descriptor = index,
                    address = (buffer as UnixByteBuffer).address,
                    count = buffer.remaining
                )
            }
        } else {
            readBytes = UnixFileChannelCalls.read(
                descriptor = index,
                addresses = buffers.map {
                    (it as UnixByteBuffer).address
                }.toLongArray(),
                count = buffers.maxBy {
                    it.remaining
                }.remaining
            )
        }

        onRead(readBytes)

        return this
    }

    override fun addBuffer(buffer: ByteBuffer): ReactiveFileChannel {
        buffers.add(buffer)
        return this
    }

    override fun removeBuffer(buffer: ByteBuffer): ReactiveFileChannel {
        buffers.remove(buffer)
        return this
    }

    override fun write(): ReactiveUnixFileChannel {
        if (buffers.isEmpty()) return this

        var writeBytes = -1

        if (buffers.size == 1) {
            val buffer = buffers.first()
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
                addresses = buffers.map {
                    (it as UnixByteBuffer).address
                }.toLongArray(),
                count = buffers.maxOf { it.remaining }
            )
        }

        onWrite(writeBytes)

        return this
    }

    override fun close() {
        if (UnixCalls.close(descriptor = index)) {
            onClose()
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