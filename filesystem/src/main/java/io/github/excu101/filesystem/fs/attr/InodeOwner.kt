package io.github.excu101.filesystem.fs.attr

interface InodeOwner {
    val inode: Long
}

fun BasicAttrs.containsInode(): Boolean = this is InodeOwner

val BasicAttrs.inode: Long
    get() = (this as InodeOwner).inode