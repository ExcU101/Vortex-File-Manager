package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.observer.PathObserverService
import io.github.excu101.filesystem.fs.path.EmptyPath
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.provider.EmptyFileSystemProvider
import io.github.excu101.filesystem.fs.utils.FileSystemHelper

object EmptyFileSystem : FileSystem(EmptyFileSystemProvider) {

    override val separator: Byte = '\u0000'.code.toByte()

    override val scheme: String = ""
    override val helper: FileSystemHelper? = null

    override val containsHelper: Boolean = false

    override fun isOpen(): Boolean = true
    override fun isReadOnly(): Boolean = true

    override val stores: Iterable<FileStore> = Iterable { iterator { } }

    override fun getPath(first: String, vararg other: String): Path = EmptyPath

    override fun newPathObserverService(): PathObserverService {
        throw UnsupportedOperationException()
    }

}