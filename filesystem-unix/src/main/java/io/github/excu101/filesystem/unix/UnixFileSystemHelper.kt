package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.FileSystemHelper
import io.github.excu101.filesystem.unix.utils.S_IFDIR
import io.github.excu101.filesystem.unix.utils.modeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UnixFileSystemHelper : FileSystemHelper() {

    override fun getCount(path: Path): Int {
        return UnixCalls.getCount(path)
    }

    override fun getDirectorySize(path: Path): Long {
        return UnixCalls.getDirectorySize(path)
    }

    override fun getDirectoryCount(path: Path): Int {
        return UnixCalls.getDirectoryCount(path)
    }

    override fun getFileCount(path: Path): Int {
        return UnixCalls.getFileCount(path)
    }

    override fun getDirectoryFlowSize(path: Path): Flow<Long> = getDirFlowSize(path.bytes)

    private fun getDirFlowSize(path: ByteArray): Flow<Long> = flow {
        val directory = UnixCalls.openDir(path)
        var entry = UnixCalls.readDir(directory)

        while (entry != null) {
            val status = UnixCalls.stat(entry.name, false)
            if (status.mode modeWith S_IFDIR) {
                getDirFlowSize(entry.name).collect(::emit)
            } else {
                emit(status.size)
            }
            entry = UnixCalls.readDir(directory)
        }

        UnixCalls.closeDir(directory)
    }

}