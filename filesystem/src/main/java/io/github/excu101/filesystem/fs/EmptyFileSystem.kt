package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.path.EmptyPath
import io.github.excu101.filesystem.fs.path.Path

object EmptyFileSystem : FileSystem(EmptyFileSystemProvider) {

    override val separator: Byte = '\u0000'.code.toByte()

    override val scheme: String = ""

    override fun isOpen(): Boolean = true
    override fun isReadOnly(): Boolean = true

    override fun getPath(first: String, vararg other: String): Path = EmptyPath

}