package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.FileStore
import io.github.excu101.filesystem.fs.FileSystemProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.EmptyAttrs
import io.github.excu101.filesystem.fs.attr.Option
import io.github.excu101.filesystem.fs.attr.StandardOptions
import io.github.excu101.filesystem.fs.channel.Channel
import io.github.excu101.filesystem.fs.channel.FileChannel
import io.github.excu101.filesystem.fs.channel.FileChannelImpl.Companion.open
import io.github.excu101.filesystem.fs.error.SystemCallException
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.attr.UnixAttributes
import io.github.excu101.filesystem.unix.path.UnixPath
import kotlin.reflect.KClass

class UnixFileSystemProvider : FileSystemProvider() {

    @Suppress(names = ["UNCHECKED_CAST"])
    override fun <T : BasicAttrs> readAttrs(source: Path, type: KClass<T>): T {
        return when (type) {
            BasicAttrs::class -> UnixAttributes.from(path = source as UnixPath, true) as T

            EmptyAttrs::class -> EmptyAttrs as T

            UnixAttributes::class -> UnixAttributes.from(path = source as UnixPath, true) as T

            else -> throw UnsupportedOperationException()
        }
    }

    override fun newFileChannel(path: Path, flags: Set<Option>, mode: Int): FileChannel {
        val readable = flags.contains(StandardOptions.READ)
        val writable = flags.contains(StandardOptions.WRITE)
        val appendable = flags.contains(StandardOptions.APPEND)

        var cFlags = if (readable && writable) 2 else if (readable) 0 else 1
        if (appendable) {
            cFlags = cFlags or 0x400
        }

        return open(
            descriptor = UnixCalls.open(path = path.bytes, flags = cFlags, mode = mode),
            path = path.toString(),
            readable = readable,
            writable = writable
        )
    }

    override fun newByteChannel(path: Path, flags: Set<Option>, mode: Int): Channel {
        return newFileChannel(path, flags, mode = 777)
    }

    override fun isHidden(source: Path): Boolean {
        return source.getName().bytes[0] == '.'.code.toByte()
    }

    override fun createDirectory(path: Path, mode: Int) {
        try {
            UnixCalls.mkdir(
                path = path.bytes,
                mode = mode
            )
        } catch (exception: SystemCallException) {
            notify(exception)
        }
    }

    override fun newDirectorySteam(path: Path): DirectoryStream<Path> = try {
        UnixDirectoryStream(
            dir = path as UnixPath, UnixCalls.openDir(path.bytes),
            onError = { error ->
                throw error
            }
        )
    } catch (exception: SystemCallException) {
        throw exception
    }

    override fun getFileStore(path: Path): FileStore {
        return UnixFileStore(path)
    }

    override val scheme: String
        get() = "file"
}