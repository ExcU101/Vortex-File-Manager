package io.github.excu101.filesystem.unix

import android.system.Int64Ref
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.structure.UnixDirectoryEntryStructure
import io.github.excu101.filesystem.unix.structure.UnixFileSystemStatusStructure
import io.github.excu101.filesystem.unix.structure.UnixStatusStructure
import java.io.File
import java.io.FileDescriptor

internal object UnixCalls {

    init {
        System.loadLibrary("unix-calls")
    }

    internal fun allocate(size: Long) = allocateImpl(size)

    private external fun allocateImpl(size: Long): Long

    fun truncate(path: ByteArray, length: Long) = truncateImpl(path, length)

    fun truncate(descriptor: Int, offset: Long) = truncateImpl(descriptor, offset)

    private external fun truncateImpl(path: ByteArray, length: Long): Int

    private external fun truncateImpl(descriptor: Int, offset: Long): Int

    external fun rename(source: ByteArray, dest: ByteArray)

    internal fun stat(
        path: ByteArray,
        isLink: Boolean,
    ): UnixStatusStructure = if (isLink) {
        lstatImpl(path)
    } else {
        statImpl(path)
    }

    private external fun statImpl(path: ByteArray): UnixStatusStructure

    private external fun lstatImpl(path: ByteArray): UnixStatusStructure

    internal fun fstat(descriptor: FileDescriptor): UnixStatusStructure = fstatImpl(descriptor)

    internal fun fstat(descriptor: Int): UnixStatusStructure = fstatImpl(descriptor)

    private external fun fstatImpl(descriptor: FileDescriptor): UnixStatusStructure

    private external fun fstatImpl(descriptor: Int): UnixStatusStructure

    internal fun lseek(
        descriptor: Int,
        offset: Long,
        whence: Int,
    ): Long = lseekImpl(descriptor, offset, whence)

    private external fun lseekImpl(descriptor: Int, offset: Long, whence: Int): Long

    external fun removeDirectory(path: ByteArray)

    external fun unlink(path: ByteArray)

    external fun openDir(path: ByteArray): Long

    internal fun open(
        path: ByteArray,
        flags: Int,
        mode: Int,
    ): FileDescriptor? = openImpl(
        path,
        flags,
        mode
    )

    private external fun openImpl(
        path: ByteArray,
        flags: Int,
        mode: Int,
    ): FileDescriptor?

    internal fun mkdir(path: ByteArray, mode: Int) = mkdirImpl(path, mode)

    private external fun mkdirImpl(path: ByteArray, mode: Int)

    internal fun symlink(target: ByteArray, link: ByteArray) = symlinkImpl(target, link)

    private external fun symlinkImpl(target: ByteArray, link: ByteArray)

    internal fun readDir(pointer: Long) = readDirImpl(pointer)

    private external fun readDirImpl(pointer: Long): UnixDirectoryEntryStructure?

    internal fun close(descriptor: Int) = closeImpl(descriptor)

    private external fun closeImpl(descriptor: Int): Boolean

    external fun closeDir(pointer: Long)

    internal fun getFileSystemStatus(path: ByteArray) = getFileSystemStatusImpl(path)

    internal fun moveBytes(
        fromDescriptor: FileDescriptor,
        toDescriptor: FileDescriptor,
        offset: Int64Ref?,
        count: Long,
    ) = moveBytesImpl(
        fromDescriptor = fromDescriptor,
        toDescriptor = toDescriptor,
        offset = offset,
        count = count
    )

    private external fun moveBytesImpl(
        fromDescriptor: FileDescriptor,
        toDescriptor: FileDescriptor,
        offset: Int64Ref?,
        count: Long,
    ): Long

    private external fun getFileSystemStatusImpl(path: ByteArray): UnixFileSystemStatusStructure

    // Helper

    internal fun getCount(path: Path): Int = getCountImpl(path.bytes)

    private external fun getCountImpl(path: ByteArray): Int

    internal fun getDirectoryCount(path: Path): Int = getDirectoryCountImpl(path.bytes)

    private external fun getDirectoryCountImpl(path: ByteArray): Int

    fun getFileCount(path: Path): Int = getFileCountImpl(path.bytes)

    private external fun getFileCountImpl(path: ByteArray): Int

    internal fun getDirectorySize(path: Path): Long = getDirectorySizeImpl(path.bytes)

    private external fun getDirectorySizeImpl(path: ByteArray): Long

    internal fun getIndexDescriptor(descriptor: FileDescriptor): Int {
        return getDescriptorImpl(descriptor)
    }

    private external fun getDescriptorImpl(descriptor: FileDescriptor): Int

}