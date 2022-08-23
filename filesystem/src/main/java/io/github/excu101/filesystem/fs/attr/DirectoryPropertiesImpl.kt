package io.github.excu101.filesystem.fs.attr

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.list

internal class DirectoryPropertiesImpl constructor(directory: Path) : DirectoryProperties {

    private val _dirs: MutableList<Path> = mutableListOf()
    private val _files: MutableList<Path> = mutableListOf()
    private val _emptyDirs: MutableList<Path> = mutableListOf()
    private val _emptyFiles: MutableList<Path> = mutableListOf()

    init {
        directory.list.forEach { path ->
            val attrs = FileProvider.readAttrs(path, BasicAttrs::class)
            if (attrs.isDirectory) {
                if (attrs.size.isEmpty()) {
                    _emptyDirs.add(path)
                }
                _dirs.add(path)
            } else {
                if (attrs.size.isEmpty()) {
                    _emptyFiles.add(path)
                }
                _files.add(path)
            }
        }
    }

    override val dirs: List<Path>
        get() = _dirs
    override val files: List<Path>
        get() = _files
    override val emptyDirs: List<Path>
        get() = _emptyDirs
    override val emptyFiles: List<Path>
        get() = _emptyFiles

}