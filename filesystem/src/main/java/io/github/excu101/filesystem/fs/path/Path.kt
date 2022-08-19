package io.github.excu101.filesystem.fs.path

import io.github.excu101.filesystem.fs.FileSystem

interface Path : Comparable<Path> {

    val parent: Path?

    val root: Path?

    val length: Int

    val isEmpty: Boolean

    val isHidden: Boolean

    val nameCount: Int

    val isAbsolute: Boolean

    val bytes: ByteArray

    val system: FileSystem

    fun startsWith(other: Path): Boolean

    fun endsWith(other: Path): Boolean

    fun resolve(other: Path): Path

    fun normalize(): Path

    fun relativize(other: Path): Path

    fun sub(from: Int, to: Int): Path

    fun getName(index: Int = nameCount - 1): Path

    override fun toString(): String

    override fun equals(other: Any?): Boolean

}