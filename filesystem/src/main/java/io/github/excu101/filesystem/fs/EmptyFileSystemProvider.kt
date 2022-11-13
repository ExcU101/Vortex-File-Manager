package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.EmptyAttrs
import io.github.excu101.filesystem.fs.channel.Channel
import io.github.excu101.filesystem.fs.channel.FileChannel
import io.github.excu101.filesystem.fs.channel.ReactiveFileChannel
import io.github.excu101.filesystem.fs.operation.FileOperation.Option
import io.github.excu101.filesystem.fs.path.Path
import kotlin.reflect.KClass

object EmptyFileSystemProvider : FileSystemProvider() {

    override fun <T : BasicAttrs> readAttrs(source: Path, type: KClass<T>): T = EmptyAttrs as T

    override fun newFileChannel(path: Path, flags: Set<Option>, mode: Int): FileChannel {
        throw UnsupportedOperationException()
    }

    override fun newReactiveFileChannel(
        path: Path,
        flags: Set<Option>,
        mode: Int,
    ): ReactiveFileChannel {
        throw UnsupportedOperationException()
    }

    override fun newByteChannel(path: Path, flags: Set<Option>, mode: Int): Channel {
        throw UnsupportedOperationException()
    }

    override fun newDirectorySteam(path: Path): DirectoryStream<Path> {
        throw UnsupportedOperationException()
    }

    override fun isHidden(source: Path): Boolean {
        return false
    }

    override val scheme: String
        get() = ""

    override fun getFileStore(path: Path): FileStore {
        throw UnsupportedOperationException()
    }
}