package io.github.excu101.filesystem.fs.channel

import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.fs.path.Path
import java.io.IOException

abstract class FileChannel protected constructor() : AbstractChannel() {

    @Throws(IOException::class)
    abstract fun read(dest: ByteBuffer): Int

    @Throws(IOException::class)
    abstract fun write(src: ByteBuffer): Int

    abstract val size: Long

    abstract val position: Long

    abstract fun newPosition(position: Long): FileChannel

    abstract fun truncate(size: Long): FileChannel

    companion object {
        fun open(path: Path) {
            path.system.provider
        }
    }

}