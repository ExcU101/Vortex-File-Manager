package io.github.excu101.filesystem.unix.structure

internal data class UnixFileSystemStatusStructure(
    val blockSize: Long,
    val fundamentBlockSize: Long,
    val totalBlocks: Long,
    val freeBlocks: Long,
    val availableBlocks: Long,
    val totalFiles: Long, // inode
    val freeFiles: Long, // inode
    val freeNonRootFiles: Long, // inode
    val fileSystemId: Long,
    val bitMask: Long,
    val maxFileNameLength: Long,
)