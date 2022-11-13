package io.github.excu101.vortex.utils

import io.github.excu101.filesystem.unix.attr.posix.PosixPermission
import io.github.excu101.filesystem.unix.attr.posix.PosixPermission.*

fun Set<PosixPermission>.convertToThemeString(): String {
    val result = StringBuilder()

    result.append(
        if (contains(Owner.READ_OWNER)) "r" else "-"
    )
    result.append(
        if (contains(Owner.WRITE_OWNER)) "w" else "-"
    )
    result.append(
        if (contains(Owner.EXECUTE_OWNER)) "x" else "-"
    )

    result.append(
        "|"
    )

    result.append(
        if (contains(Group.READ_GROUP)) "r" else "-"
    )
    result.append(
        if (contains(Group.WRITE_GROUP)) "w" else "-"
    )
    result.append(
        if (contains(Group.EXECUTE_GROUP)) "x" else "-"
    )

    result.append(
        "|"
    )

    result.append(
        if (contains(Other.READ_OTHER)) "r" else "-"
    )
    result.append(
        if (contains(Other.WRITE_OTHER)) "w" else "-"
    )
    result.append(
        if (contains(Other.EXECUTE_OTHER)) "x" else "-"
    )

    return result.toString()
}