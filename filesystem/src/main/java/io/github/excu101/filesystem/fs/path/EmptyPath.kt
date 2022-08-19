package io.github.excu101.filesystem.fs.path

import io.github.excu101.filesystem.fs.EmptyFileSystem
import io.github.excu101.filesystem.fs.FileSystem

object EmptyPath : Path {
    override val isAbsolute: Boolean = false
    override val isEmpty: Boolean = true
    override val isHidden: Boolean = false

    override val length: Int = -1
    override val nameCount: Int = -1

    override val root: Path? = null
    override val parent: Path? = null

    override val bytes: ByteArray = byteArrayOf()

    override val system: FileSystem = EmptyFileSystem

    override fun getName(index: Int): Path = this
    override fun normalize(): Path = this
    override fun relativize(other: Path): Path = this
    override fun resolve(other: Path): Path = this
    override fun sub(from: Int, to: Int): Path = this

    override fun compareTo(other: Path): Int = 0

    override fun toString(): String = ""

    override fun equals(other: Any?): Boolean = false
    override fun startsWith(other: Path): Boolean = false
    override fun endsWith(other: Path): Boolean = false
}