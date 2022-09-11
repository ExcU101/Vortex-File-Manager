package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.FileSystemHelper

class UnixFileSystemHelper : FileSystemHelper() {

    override fun getCount(path: Path): Int {
        return UnixCalls.getCount(path)
    }

    override fun getDirectoryCount(path: Path): Int {
        return UnixCalls.getDirectoryCount(path)
    }

    override fun getFileCount(path: Path): Int {
        return UnixCalls.getFileCount(path)
    }

}