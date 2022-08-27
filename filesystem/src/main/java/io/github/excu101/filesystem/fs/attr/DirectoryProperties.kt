package io.github.excu101.filesystem.fs.attr

import io.github.excu101.filesystem.fs.path.Path

interface DirectoryProperties {

    val dirs: List<Path>

    val files: List<Path>

    val emptyDirs: List<Path>

    val emptyFiles: List<Path>

    val dirsCount: Int
        get() = dirs.size

    val filesCount: Int
        get() = files.size

    val count: Int
        get() = dirsCount + filesCount
}