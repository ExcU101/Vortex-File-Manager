package io.github.excu101.filesystem.unix.utils

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.attr.posix.PosixPermission

fun Array<PosixPermission>.parseToString() = StringBuilder()
    .append(if (contains(PosixPermission.Owner.READ_OWNER)) "r" else "-")
    .append(if (contains(PosixPermission.Owner.WRITE_OWNER)) "w" else "-")
    .append(if (contains(PosixPermission.Owner.EXECUTE_OWNER)) "e" else "-")
    .append(if (contains(PosixPermission.Group.READ_GROUP)) "r" else "-")
    .append(if (contains(PosixPermission.Group.WRITE_GROUP)) "w" else "-")
    .append(if (contains(PosixPermission.Group.EXECUTE_GROUP)) "e" else "-")
    .append(if (contains(PosixPermission.Other.READ_OTHER)) "r" else "-")
    .append(if (contains(PosixPermission.Other.WRITE_OTHER)) "w" else "-")
    .append(if (contains(PosixPermission.Other.EXECUTE_OTHER)) "e" else "-")
    .toString()

fun Path.findContent() {
    val list = mutableListOf<Path>()
    FileProvider.newDirStream(this).use { stream ->
        stream.forEach { path ->
            val attrs = FileProvider.readAttrs(path, BasicAttrs::class)
        }
    }
}