package io.github.excu101.filesystem.fs.provider

import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.DirectoryStream.Filter
import io.github.excu101.filesystem.fs.FileStore
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.channel.AsyncFileChannel
import io.github.excu101.filesystem.fs.channel.Channel
import io.github.excu101.filesystem.fs.channel.FileChannel
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.PathOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

abstract class FileSystemProvider {

    open suspend fun runOperation(
        operation: FileOperation,
        observers: List<PathOperationObserver>,
    ) = withContext(IO) {
        operation.subscribe(observers)
        operation.perform()
    }

    abstract fun <T : BasicAttrs> readAttrs(source: Path, type: KClass<T>): T

    abstract fun newFileChannel(
        path: Path,
        flags: Int,
        mode: Int,
    ): FileChannel

    abstract fun newByteChannel(
        path: Path,
        flags: Int,
        mode: Int,
    ): Channel

    abstract fun newDirectorySteam(path: Path): DirectoryStream<Path>

    open fun newDirectoryFlow(
        directory: Path,
        filter: Filter<Path> = Filter.acceptAll(),
    ): Flow<Path> = emptyFlow()

    abstract fun newReactiveFileChannel(
        path: Path,
        flags: Int,
        mode: Int,
    ): AsyncFileChannel

    abstract fun isHidden(source: Path): Boolean

    abstract val scheme: String

    abstract fun getFileStore(path: Path): FileStore
}