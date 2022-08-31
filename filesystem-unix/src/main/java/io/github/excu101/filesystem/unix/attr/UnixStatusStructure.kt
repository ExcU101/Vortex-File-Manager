package io.github.excu101.filesystem.unix.attr

data class UnixStatusStructure(
    val userId: Int,
    val groupId: Int,
    val mode: Int,
    val deviceId: Long,
    val inode: Long,
    val countLinks: Long,
    val otherDeviceId: Long,
    val blocksSize: Long,
    val blocks: Long,
    val size: Long,
    val lastModifiedTimeSeconds: Long,
    val lastAccessTimeSeconds: Long,
    val creationTimeSeconds: Long,
    val lastModifiedTimeNanos: Long,
    val lastAccessTimeNanos: Long,
    val creationTimeNanos: Long,
)