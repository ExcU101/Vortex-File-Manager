package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.Option
import io.github.excu101.filesystem.fs.channel.Channel
import io.github.excu101.filesystem.fs.channel.FileChannel
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

abstract class FileSystemProvider {

    open fun runOperation(
        operation: FileOperation,
        observers: List<FileOperationObserver>,
    ) = CoroutineScope(IO).launch {
        operation.subscribe(observers)
        operation.perform()
    }

    abstract fun <T : BasicAttrs> readAttrs(source: Path, type: KClass<T>): T

    abstract fun newFileChannel(path: Path, flags: Set<Option>, mode: Int): FileChannel

    abstract fun newByteChannel(path: Path, flags: Set<Option>, mode: Int): Channel

    abstract fun newDirectorySteam(path: Path): DirectoryStream<Path>

    abstract fun isHidden(source: Path): Boolean

    abstract val scheme: String

    abstract fun createDirectory(path: Path, mode: Int)

    abstract fun getFileStore(path: Path): FileStore

    protected fun notify(error: Throwable) {
        FileProvider.notify(error = error)
    }
}