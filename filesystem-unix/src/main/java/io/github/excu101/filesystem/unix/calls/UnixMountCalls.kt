package io.github.excu101.filesystem.unix.calls

import io.github.excu101.filesystem.unix.structure.UnixMountEntryStructure

internal object UnixMountCalls {

    init {
        System.loadLibrary("unix-calls")
    }

    internal fun openMountEntryPointer(path: ByteArray, type: ByteArray) = openMountEntryPointerImpl(path, type)

    private external fun openMountEntryPointerImpl(path: ByteArray, type: ByteArray): Long

    internal fun getMountEntry(pointer: Long) = getMountEntryImpl(pointer)

    private external fun getMountEntryImpl(pointer: Long): UnixMountEntryStructure?

    internal fun closeMountEntryPointer(pointer: Long) = closeMountEntryPointerImpl(pointer)

    private external fun closeMountEntryPointerImpl(pointer: Long)

}