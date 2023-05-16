package io.github.excu101.filesystem.unix.attr

import io.github.excu101.filesystem.fs.attr.InodeOwner
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.attr.time.FileTime
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.attr.posix.PosixAttrs
import io.github.excu101.filesystem.unix.attr.posix.PosixGroup
import io.github.excu101.filesystem.unix.attr.posix.PosixPermission
import io.github.excu101.filesystem.unix.path.UnixPath
import io.github.excu101.filesystem.unix.structure.UnixStatusStructure
import io.github.excu101.filesystem.unix.utils.*

internal class UnixAttributes private constructor(
    private val structure: UnixStatusStructure,
) : PosixAttrs, InodeOwner {

    companion object {
        fun from(path: UnixPath, followLinks: Boolean): UnixAttributes {
            return UnixAttributes(
                structure = UnixCalls.stat(path.bytes, isLink = !followLinks),
            )
        }
    }

    private var _perms = mutableSetOf<PosixPermission>()

    init {

        if (structure.mode and S_IRUSR > 0) {
            _perms += PosixPermission.Owner.READ_OWNER
        }
        if (structure.mode and S_IWUSR > 0) {
            _perms += PosixPermission.Owner.WRITE_OWNER
        }
        if (structure.mode and S_IXUSR > 0) {
            _perms += PosixPermission.Owner.EXECUTE_OWNER
        }

        if (structure.mode and S_IRGRP > 0) {
            _perms += PosixPermission.Group.READ_GROUP
        }
        if (structure.mode and S_IWGRP > 0) {
            _perms += PosixPermission.Group.WRITE_GROUP
        }
        if (structure.mode and S_IXGRP > 0) {
            _perms += PosixPermission.Group.EXECUTE_GROUP
        }

        if (structure.mode and S_IROTH > 0) {
            _perms += PosixPermission.Other.READ_OTHER
        }
        if (structure.mode and S_IWOTH > 0) {
            _perms += PosixPermission.Other.WRITE_OTHER
        }
        if (structure.mode and S_IXOTH > 0) {
            _perms += PosixPermission.Other.EXECUTE_OTHER
        }
    }

    override val group: PosixGroup
        get() {
            val g = UnixCalls.getGroup(structure.groupId)
            return PosixGroup(g.id, g.name.decodeToString())
        }

    override val owner: String
        get() = structure.userId.toString()

    override val perms: Set<PosixPermission>
        get() = _perms

    override val inode: Long
        get() = structure.inode

    override val isDirectory: Boolean
        get() = structure.mode modeWith S_IFDIR

    override val isFile: Boolean
        get() = structure.mode modeWith S_IFREG

    override val isLink: Boolean
        get() = structure.mode modeWith S_IFLNK

    override val isOther: Boolean
        get() = !isDirectory && !isFile && !isLink

    override val lastModifiedTime: FileTime
        get() = FileTime(
            seconds = structure.lastModifiedTimeSeconds,
            nanos = structure.lastModifiedTimeNanos
        )

    override val lastAccessTime: FileTime
        get() = FileTime(
            seconds = structure.lastModifiedTimeSeconds,
            nanos = structure.lastModifiedTimeNanos
        )

    override val creationTime: FileTime
        get() = FileTime(
            seconds = structure.lastAccessTimeSeconds,
            nanos = structure.lastAccessTimeNanos
        )

    override val size: Size
        get() = Size(memory = structure.size)
}