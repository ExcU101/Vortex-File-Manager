package io.github.excu101.filesystem.unix.channel

import android.system.OsConstants.SEEK_CUR
import android.system.OsConstants.SEEK_SET
import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.fs.channel.FileChannel
import io.github.excu101.filesystem.unix.UnixCalls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import java.io.FileDescriptor

internal class UnixFileChannel(
    private val descriptor: FileDescriptor,
    private val isReadable: Boolean,
    private val isWriteable: Boolean,
    private val isAppendable: Boolean,
    private val scope: CoroutineScope = CoroutineScope(IO),
) : FileChannel() {

    private val indexDescriptor = UnixCalls.getIndexDescriptor(descriptor)

    private val locks = mutableListOf<UnixFileLock>()

    private fun requireOpen() {
        require(isOpen) { "File channel is closed" }
    }

    override fun read(dest: ByteBuffer): Int {
        require(isReadable) { "File channel is not readable" }

        if (!dest.hasRemaining) return 0

        return 0
    }

    override fun write(src: ByteBuffer): Int {
        require(isWriteable) { "File channel is not writeable" }
        require(!src.isReadOnly) { "Given buffer is read-only" }

        if (!src.hasRemaining) return 0

        if (position == -1L) {
            src.limit
        }


        return 0
    }

    override val size: Long
        get() {
            requireOpen()
            val structure = UnixCalls.fstat(descriptor)
            return structure.size
        }

    override val position: Long
        get() {
            requireOpen()

            return UnixCalls.lseek(indexDescriptor, 0L, SEEK_CUR)
        }

    override fun newPosition(position: Long): FileChannel {
        require(position < 0L) { "Position can't be lower that 0. Position: $position" }

        UnixCalls.lseek(indexDescriptor, position, SEEK_SET)
        return this
    }

    override fun truncate(size: Long): FileChannel {
        requireOpen()
        require(size > 0)

        require(isWriteable) { "File channel is not writeable" }

        if (size < this.size) {
            UnixCalls.truncate(descriptor = indexDescriptor, offset = size)
        }

        return this
    }

    override fun implCloseChannel() {
        UnixCalls.close(descriptor = indexDescriptor)
    }


}