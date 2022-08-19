package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.path.Path

data class FileUnit(
    val path: Path,
) {
    val attrs: BasicAttrs
        get() = FileProvider.readAttrs(path)

    val isDirectory: Boolean
        get() = attrs.isDirectory

    val isFile: Boolean
        get() = attrs.isFile

    val isLink: Boolean
        get() = attrs.isLink

    val parent: FileUnit?
        get() = path.parent?.let(::FileUnit)

    val name: String
        get() = path.getName().toString()

    val size: Size
        get() = attrs.size

    val mimeType: MimeType
        get() = MimeType.fromName(name)
}