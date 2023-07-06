package io.github.excu101.filesystem.fs.attr

import io.github.excu101.filesystem.fs.attr.time.FileTime
import io.github.excu101.filesystem.fs.attr.size.Size

interface BasicAttrs {

    val isDirectory: Boolean

    val isFile: Boolean

    val isLink: Boolean

    val isOther: Boolean

    val lastModifiedTime: FileTime

    val lastAccessTime: FileTime

    val creationTime: FileTime

    val size: Size

}