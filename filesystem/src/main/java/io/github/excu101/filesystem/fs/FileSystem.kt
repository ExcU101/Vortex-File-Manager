package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.observer.PathObserverService
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.provider.FileSystemProvider
import io.github.excu101.filesystem.fs.utils.FileSystemHelper

abstract class FileSystem(
    open val provider: FileSystemProvider,
) {

    abstract val separator: Byte

    abstract val scheme: String

    abstract val helper: FileSystemHelper?

    open val containsHelper: Boolean
        get() = helper != null

    abstract val stores: Iterable<FileStore>

    abstract fun isOpen(): Boolean

    abstract fun isReadOnly(): Boolean

    abstract fun getPath(first: String, vararg other: String): Path

    abstract fun newPathObserverService(): PathObserverService

}