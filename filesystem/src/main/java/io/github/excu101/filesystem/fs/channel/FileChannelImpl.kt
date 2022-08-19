package io.github.excu101.filesystem.fs.channel

import android.os.ParcelFileDescriptor
import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.fs.buffer.HeapByteBuffer
import io.github.excu101.filesystem.fs.buffer.buffer
import io.github.excu101.filesystem.fs.operation.NativeCalls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import java.io.FileDescriptor

class FileChannelImpl private constructor(
    val descriptor: FileDescriptor,
    val path: String,
    val readable: Boolean,
    val writable: Boolean,
    val append: Boolean = false,
) : FileChannel() {

    val scope = CoroutineScope(IO)

    @OptIn(ObsoleteCoroutinesApi::class)
    private val inputChannel = scope.actor<Byte>(capacity = Channel.BUFFERED) {

    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private val outputChannel = scope.actor<Byte>(capacity = Channel.BUFFERED) {

    }

    @Volatile
    private var open = true

    companion object {
        fun open(
            descriptor: FileDescriptor,
            path: String,
            readable: Boolean = true,
            writable: Boolean = true,
            append: Boolean = false,
        ): FileChannel = FileChannelImpl(
            descriptor = descriptor,
            path = path,
            readable = readable,
            writable = writable,
            append = append
        )
    }

    override fun read(dest: ByteBuffer): Int {
        require(readable)

        if (!isOpen) return 0

        val position = dest.position.toLong()
        val limit = dest.limit.toLong()
        val remaining = if (position <= limit) limit - position else 0
        val address = if (dest is HeapByteBuffer) dest.capacity.toLong() else 0L

        buffer(capacity = 100) {

        }

        return NativeCalls.pointerRead(
            descriptor = NativeCalls.getFileDescriptor(descriptor),
            address = address,
            length = remaining,
            position = position
        )
    }

    override fun write(src: ByteBuffer): Int {
        require(writable)
        return 0
    }

    override val size: Long
        get() = 0L

    override val position: Int
        get() = 0

    override fun newPosition(position: Int): FileChannel {
        return this
    }

    override fun truncate(size: Long): FileChannel {
        return this
    }

    override val isOpen: Boolean
        get() = open

    override fun implCloseChannel() {
        NativeCalls.close(ParcelFileDescriptor.dup(descriptor).fd)
    }

    override fun close() {
        implCloseChannel()
    }

}