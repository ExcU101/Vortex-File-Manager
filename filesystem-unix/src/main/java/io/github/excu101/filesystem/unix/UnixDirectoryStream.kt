package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.error.DirectoryAlreadyClosedException
import io.github.excu101.filesystem.fs.error.SystemCallException
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.resolve
import io.github.excu101.filesystem.unix.path.UnixPath
import java.io.IOException

class UnixDirectoryStream internal constructor(
    private val dir: UnixPath,
    private val pointer: Long,
    private val filter: DirectoryStream.Filter<Path> = DirectoryStream.Filter.acceptAll(),
) : DirectoryStream<Path> {

    private var iterator: LinuxPathIterator? = null

    companion object {
        val DOT = byteArrayOf('.'.code.toByte())
        val DOUBLE_DOT = "..".toByteArray()
    }

    private var isClosed: Boolean = false
    private val locker = Any()

    override fun close() {
        synchronized(locker) {
            if (isClosed) return
            try {
                UnixCalls.closeDir(pointer = pointer)
            } catch (exception: SystemCallException) {
                throw exception
            }
            isClosed = true
        }
    }

    override fun iterator(): Iterator<Path> {
        synchronized(locker) {
            if (isClosed) {
                throw DirectoryAlreadyClosedException()
            }
            if (iterator == null) {

            }
            return LinuxPathIterator().also { iterator = it }
        }

    }

    private inner class LinuxPathIterator : MutableIterator<UnixPath> {
        private var nextPath: UnixPath? = null
        private var isFinished = false

        override fun next(): UnixPath {
            synchronized(locker) {
                if (!hasNext()) {
                    throw Throwable("Doesn't have next")
                }
                val path = nextPath!!
                nextPath = null
                return path
            }

        }

        override fun hasNext(): Boolean {
            if (nextPath != null) return true

            if (isFinished) return false
            nextPath = readNext()
            isFinished = nextPath == null
            return !isFinished
        }

        private fun readNext(): UnixPath? {
            while (true) {
                if (isClosed) {
                    return null
                }
                val dirent = try {
                    UnixCalls.readDir(pointer = pointer) ?: return null
                } catch (exception: SystemCallException) {
                    throw exception
                }

                if (dirent.name.contentEquals(DOT) || dirent.name.contentEquals(DOUBLE_DOT)) {
                    continue
                }

                val path = dir.resolve(
                    bytes = dirent.name
                ) as UnixPath
                val accepted = try {
                    filter.accept(path)
                } catch (exception: IOException) {
                    throw exception
                }
                if (!accepted) continue

                return path
            }

        }

        override fun remove() {

        }
    }

}