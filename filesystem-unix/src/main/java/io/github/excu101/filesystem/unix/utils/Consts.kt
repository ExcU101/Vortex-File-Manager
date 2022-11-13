@file:JvmName("UnixConsts")

package io.github.excu101.filesystem.unix.utils

// Type file
internal const val S_IFMT: Int = 0xf000

// Regular file
internal const val S_IFREG: Int = 0x8000

// Directory file
internal const val S_IFDIR: Int = 0x4000

// Link
internal const val S_IFLNK: Int = 0xA000

// Permissions
// User
internal const val S_IRUSR: Int = 256
internal const val S_IWUSR: Int = 128
internal const val S_IXUSR: Int = 64

// Group
internal const val S_IRGRP: Int = 32
internal const val S_IWGRP: Int = 16
internal const val S_IXGRP: Int = 8

// Group
internal const val S_IROTH: Int = 4
internal const val S_IWOTH: Int = 2
internal const val S_IXOTH: Int = 1


internal infix fun Int.modeWith(other: Int): Boolean {
    return (this and S_IFMT) == other
}