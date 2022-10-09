package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.fs.FileStore
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.attr.UnixStructureFileSystemStatus

internal class UnixFileStore(val path: Path) : FileStore() {

    private val structure: UnixStructureFileSystemStatus
        get() = UnixCalls.statVfs(path = path.bytes)

    override val totalSpace: Long
        get() = structure.totalBlocks * structure.blockSize

    override val usableSpace: Long
        get() = structure.availableBlocks * structure.blockSize

    override val unallocatedSpace: Long
        get() = structure.freeBlocks * structure.blockSize

}