package io.github.excu101.filesystem.unix

import android.system.OsConstants.O_CREAT
import android.system.OsConstants.O_EXCL
import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.FileStore
import io.github.excu101.filesystem.fs.FileSystemProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.EmptyAttrs
import io.github.excu101.filesystem.fs.attr.Option
import io.github.excu101.filesystem.fs.attr.StandardOptions
import io.github.excu101.filesystem.fs.channel.Channel
import io.github.excu101.filesystem.fs.channel.FileChannel
import io.github.excu101.filesystem.fs.channel.ReactiveFileChannel
import io.github.excu101.filesystem.fs.error.SystemCallException
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.attr.UnixAttributes
import io.github.excu101.filesystem.unix.attr.posix.PosixAttrs
import io.github.excu101.filesystem.unix.channel.ReactiveUnixFileChannel
import io.github.excu101.filesystem.unix.channel.UnixFileChannel
import io.github.excu101.filesystem.unix.path.UnixPath
import kotlin.reflect.KClass


class UnixFileSystemProvider : FileSystemProvider() {

    @Suppress(names = ["UNCHECKED_CAST"])
    override fun <T : BasicAttrs> readAttrs(source: Path, type: KClass<T>): T {
        return when (type) {
            BasicAttrs::class -> UnixAttributes.from(path = source as UnixPath, true) as T

            EmptyAttrs::class -> EmptyAttrs as T

            PosixAttrs::class -> UnixAttributes.from(path = source as UnixPath, true) as T

            else -> throw UnsupportedOperationException()
        }
    }

    override fun newReactiveFileChannel(
        path: Path,
        flags: Set<Option>,
        mode: Int,
    ): ReactiveFileChannel {
        val readable = flags.contains(StandardOptions.READ)
        val writable = flags.contains(StandardOptions.WRITE)
        val appendable = flags.contains(StandardOptions.APPEND)
        val createNew = flags.contains(StandardOptions.CREATE_NEW)

        var cFlags = if (readable && writable) 2 else if (readable) 0 else 1
        if (appendable) {
            cFlags = cFlags or 0x400
        }

        if (createNew) {
            cFlags = cFlags or O_CREAT or O_EXCL
        }

        val descriptor = UnixCalls.open(
            path = path.bytes,
            flags = cFlags,
            mode = mode
        ) ?: throw IllegalArgumentException()

        return ReactiveUnixFileChannel(
            descriptor = descriptor
        )
    }

    override fun newFileChannel(path: Path, flags: Set<Option>, mode: Int): FileChannel {
        val readable = flags.contains(StandardOptions.READ)
        val writable = flags.contains(StandardOptions.WRITE)
        val appendable = flags.contains(StandardOptions.APPEND)
        val createNew = flags.contains(StandardOptions.CREATE_NEW)

        var cFlags = if (readable && writable) 2 else if (readable) 0 else 1
        if (appendable) {
            cFlags = cFlags or 0x400
        }
        if (createNew) {
            cFlags = cFlags or O_CREAT or O_EXCL
        }

        val descriptor = UnixCalls.open(
            path = path.bytes,
            flags = cFlags,
            mode = mode
        ) ?: throw IllegalArgumentException()

        return UnixFileChannel(
            descriptor = descriptor,
            isReadable = readable,
            isWriteable = writable,
            isAppendable = appendable
        )
    }

    override fun newByteChannel(path: Path, flags: Set<Option>, mode: Int): Channel {
        return newFileChannel(path, flags, mode = 777)
    }

    override fun isHidden(source: Path): Boolean {
        return source.getName().bytes[0] == '.'.code.toByte()
    }

    override fun newDirectorySteam(path: Path): DirectoryStream<Path> = try {
        UnixDirectoryStream(
            dir = path as UnixPath, UnixCalls.openDir(path.bytes),
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