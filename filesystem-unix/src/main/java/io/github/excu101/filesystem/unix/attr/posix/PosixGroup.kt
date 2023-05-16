package io.github.excu101.filesystem.unix.attr.posix

interface PosixGroup {
    val id: Int

    val name: String
}

fun PosixGroup(
    id: Int,
    name: String
): PosixGroup = object : PosixGroup {
    override val id: Int = id
    override val name: String = name
}