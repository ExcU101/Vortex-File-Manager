package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.attr.UnixDirentStructure
import io.github.excu101.filesystem.unix.attr.UnixStatusStructure
import io.github.excu101.filesystem.unix.attr.UnixStructureFileSystemStatus
import io.github.excu101.filesystem.unix.error.UnixException
import java.io.FileDescriptor

internal object UnixCalls {

    init {
        System.loadLibrary("unix-calls")
    }

    external fun rename(source: ByteArray, dest: ByteArray)

    external fun stat(path: ByteArray): UnixStatusStructure

    external fun lstat(path: ByteArray): UnixStatusStructure

    external fun removeDirectory(path: ByteArray)

    external fun unlink(path: ByteArray)

    external fun openDir(path: ByteArray): Long

    external fun open(path: ByteArray, flags: Int, mode: Int): FileDescriptor

    external fun mkdir(path: ByteArray, mode: Int)

    external fun readDir(pointer: Long): UnixDirentStructure?

    external fun close(descriptor: Int)

    external fun closeDir(pointer: Long)

    external fun statVfs(path: ByteArray): UnixStructureFileSystemStatus

    internal fun getCount(path: Path): Int = getCountImpl(path.bytes)

    private external fun getCountImpl(path: ByteArray): Int

    internal fun getDirectoryCount(path: Path): Int = getDirectoryCountImpl(path.bytes)

    private external fun getDirectoryCountImpl(path: ByteArray): Int

    fun getFileCount(path: Path): Int = getFileCountImpl(path.bytes)

    private external fun getFileCountImpl(path: ByteArray): Int

}