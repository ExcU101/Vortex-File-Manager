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

    val scheme: String
        get() = system.scheme

    fun getParentAt(index: Int): Path?

    infix fun startsWith(other: Path): Boolean

    infix fun endsWith(other: Path): Boolean

    infix fun resolve(other: Path): Path

    fun normalize(): Path

    infix fun relativize(other: Path): Path

    fun sub(from: Int, to: Int): Path

    fun getName(index: Int = nameCount - 1): Path

    override fun hashCode(): Int

    override fun toString(): String

    override fun equals(other: Any?): Boolean

}