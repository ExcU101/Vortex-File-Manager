package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.path.Path

abstract class FileSystem(
    open val provider: FileSystemProvider
) {

    abstract val separator: Byte

    abstract val scheme: String

    abstract fun isOpen(): Boolean

    abstract fun isReadOnly(): Boolean

    abstract fun getPath(first: String, vararg other: String): Path

}