package io.github.excu101.filesystem.fs.attr

import io.github.excu101.filesystem.fs.attr.time.FileTime
import io.github.excu101.filesystem.fs.attr.size.SiSize
import io.github.excu101.filesystem.fs.attr.size.Size

object EmptyAttrs : BasicAttrs {

    override val isDirectory: Boolean
        get() = false
    override val isFile: Boolean
        get() = false
    override val isLink: Boolean
        get() = false
    override val isOther: Boolean
        get() = false
    override val lastModifiedTime: FileTime
        get() = FileTime(seconds = 0L, nanos = 0L)
    override val lastAccessTime: FileTime
        get() = FileTime(seconds = 0L, nanos = 0L)
    override val creationTime: FileTime
        get() = FileTime(seconds = 0L, nanos = 0L)
    override val size: Size
        get() = SiSize(0L)
}