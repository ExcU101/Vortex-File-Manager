package io.github.excu101.filesystem.unix.attr.posix

import io.github.excu101.filesystem.fs.attr.BasicAttrs

interface PosixAttrs : BasicAttrs {

    val group: String

    val owner: String

    val perms: Set<PosixPermission>

}

