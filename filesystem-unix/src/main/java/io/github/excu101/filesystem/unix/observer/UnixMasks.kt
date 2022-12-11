package io.github.excu101.filesystem.unix.observer

import io.github.excu101.filesystem.unix.observer.UnixMasks.maskWith

object UnixMasks {
    const val POLL_IN = 0x0001
//    const val POLL_IN = 0x0001

    // Some files were edited
    const val MODIFY = 0x00000002

    // Something was created
    const val CREATE = 0x00000100

    // Something was deleted
    const val DELETE = 0x00000200

    const val ACCESS = 0x00000001
    const val ATTRIB = 0x00000004
    const val CLOSE_WRITE = 0x00000008
    const val CLOSE_NOWRITE = 0x00000010
    const val OPEN = 0x00000020
    const val MOVED_FROM = 0x00000040
    const val MOVED_TO = 0x00000080
    const val DELETE_SELF = 0x00000400
    const val MOVE_SELF = 0x00000800
    const val UNMOUNT = 0x00002000
    const val OVERFLOW = 0x00004000
    const val IGNORED = 0x00008000
    const val CLOSE = CLOSE_WRITE or CLOSE_NOWRITE
    const val MOVE = MOVED_FROM or MOVED_TO
    const val ONLYDIR = 0x01000000
    const val DONT_FOLLOW = 0x02000000
    const val EXCL_UNLINK = 0x04000000
    const val MASK_CREATE = 0x10000000
    const val MASK_ADD = 0x20000000
    const val ISDIR = 0x40000000

    internal infix fun Int.maskWith(other: Int): Boolean {
        return (this and other) == other
    }
}