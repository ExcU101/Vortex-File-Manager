package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.fs.FileStore
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.toPath
import io.github.excu101.filesystem.unix.calls.UnixMountCalls.closeMountEntryPointer
import io.github.excu101.filesystem.unix.calls.UnixMountCalls.getMountEntry
import io.github.excu101.filesystem.unix.calls.UnixMountCalls.openMountEntryPointer
import io.github.excu101.filesystem.unix.structure.UnixFileSystemStatusStructure
import io.github.excu101.filesystem.unix.structure.UnixMountEntryStructure

// I hate file systems...

internal class UnixFileStore : FileStore {

    companion object {
        fun from(system: UnixFileSystem, entry: UnixMountEntryStructure) = UnixFileStore(
            system = system,
            entry = entry
        )

        fun from(path: Path): UnixFileStore = UnixFileStore(
            path = path
        )

        private val defaultPath = "/proc/self/mounts".toPath()
        private val defaultMode = "r".toPath()

        inline val entries: List<UnixMountEntryStructure>
            get() {
                val _entries = mutableListOf<UnixMountEntryStructure>()

                val pointer = openMountEntryPointer(defaultPath.bytes, defaultMode.bytes)

                try {
                    while (true) {
                        _entries += getMountEntry(pointer) ?: break
                    }
                } finally {
                    closeMountEntryPointer(pointer)
                }

                return _entries
            }
    }

    private constructor(path: Path) {
        this.path = path
        this.entry = resolveEntry()
    }

    private constructor(system: UnixFileSystem, entry: UnixMountEntryStructure) {
        this.path = system.getPath(entry.getCachedDir())
        this.entry = entry
    }

    private val path: Path
    private var entry: UnixMountEntryStructure? = null

    private fun resolveEntry(): UnixMountEntryStructure? {
        val map = mutableMapOf<Path, UnixMountEntryStructure>()

        for (entry in entries) {
            val entryPath = path.system.getPath(entry.getCachedDir())
            map[entryPath] = entry
        }
        var path = path
        while (true) {
            val entry = map[path]
            if (entry != null) {
                return entry
            }
            path = path.parent ?: break
        }
        return null
    }

    private val status: UnixFileSystemStatusStructure
        get() = UnixCalls.getFileSystemStatus(path = path.bytes)

    override val name: String
        get() = entry?.getCachedName() ?: ""

    override val type: String
        get() = entry?.getCachedType() ?: ""

    override val totalSpace: Long
        get() = status.totalBlocks * status.blockSize

    override val usableSpace: Long
        get() = status.availableBlocks * status.blockSize

    override val unallocatedSpace: Long
        get() = status.freeBlocks * status.blockSize

}