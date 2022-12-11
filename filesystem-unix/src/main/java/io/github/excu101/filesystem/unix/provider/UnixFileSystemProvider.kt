package io.github.excu101.filesystem.unix.provider

import android.system.OsConstants.O_CREAT
import android.system.OsConstants.O_EXCL
import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.DirectoryStream.Filter
import io.github.excu101.filesystem.fs.FileStore
import io.github.excu101.filesystem.fs.provider.FileSystemProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.EmptyAttrs
import io.github.excu101.filesystem.fs.channel.Channel
import io.github.excu101.filesystem.fs.channel.FileChannel
import io.github.excu101.filesystem.fs.channel.AsyncFileChannel
import io.github.excu101.filesystem.fs.error.SystemCallException
import io.github.excu101.filesystem.fs.operation.FileOperation.Option
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.resolve
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.UnixDirectoryStream
import io.github.excu101.filesystem.unix.UnixFileStore
import io.github.excu101.filesystem.unix.attr.UnixAttributes
import io.github.excu101.filesystem.unix.attr.posix.PosixAttrs
import io.github.excu101.filesystem.unix.channel.AsyncUnixFileChannel
import io.github.excu101.filesystem.unix.channel.UnixFileChannel
import io.github.excu101.filesystem.unix.path.UnixPath
import io.github.excu101.filesystem.unix.structure.UnixDirectoryEntryStructure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    ): AsyncFileChannel {
        val readable = flags.contains(Options.Open.Read)
        val writable = flags.contains(Options.Open.Write)
        val appendable = flags.contains(Options.Open.Append)
        val createNew = flags.contains(Options.Open.CreateNew)

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

        return AsyncUnixFileChannel(
            descriptor = descriptor
        )
    }

    override fun newFileChannel(path: Path, flags: Set<Option>, mode: Int): FileChannel {
        val readable = flags.contains(Options.Open.Read)
        val writable = flags.contains(Options.Open.Write)
        val appendable = flags.contains(Options.Open.Append)
        val createNew = flags.contains(Options.Open.CreateNew)

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

    override fun newDirectorySteam(path: Path): DirectoryStream<Path> {
        return try {
            UnixDirectoryStream(
                dir = path as UnixPath, pointer = UnixCalls.openDir(path.bytes),
            )
        } catch (exception: SystemCallException) {
            throw exception
        }
    }

    override fun newDirectoryFlow(
        directory: Path,
        filter: Filter<Path>,
    ): Flow<Path> = flow {
        val pointer = UnixCalls.openDir(directory.bytes)

        var entry = getNextEntry(pointer)
        while (entry != null) {
            val path = directory.resolve(entry.name)

            if (filter.accept(path)) {
                emit(path)
            }
            entry = getNextEntry(pointer)
        }

        UnixCalls.closeDir(pointer)
    }

    private fun getNextEntry(
        pointer: Long,
    ): UnixDirectoryEntryStructure? {
        return UnixCalls.readDir(pointer)
    }

    override fun getFileStore(path: Path): FileStore {
        return UnixFileStore.from(path)
    }

    override val scheme: String
        get() = "unix"
}