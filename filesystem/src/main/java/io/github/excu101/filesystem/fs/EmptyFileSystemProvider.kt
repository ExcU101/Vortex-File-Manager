package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.EmptyAttrs
import io.github.excu101.filesystem.fs.attr.Option
import io.github.excu101.filesystem.fs.channel.Channel
import io.github.excu101.filesystem.fs.channel.FileChannel
import io.github.excu101.filesystem.fs.channel.ReactiveFileChannel
import io.github.excu101.filesystem.fs.path.Path
import kotlin.reflect.KClass

object EmptyFileSystemProvider : FileSystemProvider() {

    override fun <T : BasicAttrs> readAttrs(source: Path, type: KClass<T>): T = EmptyAttrs as T

    override fun newFileChannel(path: Path, flags: Set<Option>, mode: Int): FileChannel {
        TODO("Not yet implemented")
    }

    override fun newReactiveFileChannel(
        path: Path,
        flags: Set<Option>,
        mode: Int
    ): ReactiveFileChannel {
        TODO("Not yet implemented")
    }

    override fun newByteChannel(path: Path, flags: Set<Option>, mode: Int): Channel {
        TODO("Not yet implemented")
    }

    override fun newDirectorySteam(path: Path): DirectoryStream<Path> {
        TODO("Not yet implemented")
    }

    override fun isHidden(source: Path): Boolean {
        TODO("Not yet implemented")
    }

    override val scheme: String
        get() = ""

    override fun getFileStore(path: Path): FileStore {
        TODO("Not yet implemented")
    }
}