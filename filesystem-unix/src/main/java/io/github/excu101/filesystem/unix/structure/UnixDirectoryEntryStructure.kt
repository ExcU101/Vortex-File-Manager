package io.github.excu101.filesystem.unix.structure

internal data class UnixDirectoryEntryStructure(
    val inode: Long,
    val offset: Long,
    val recordLength: Int,
    val type: Int,
    val name: ByteArray,
)