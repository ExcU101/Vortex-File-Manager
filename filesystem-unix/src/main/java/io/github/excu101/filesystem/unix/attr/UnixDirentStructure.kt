package io.github.excu101.filesystem.unix.attr

data class UnixDirentStructure(
    val inode: Long,
    val offset: Long,
    val recordLength: Int,
    val type: Int,
    val name: ByteArray
)