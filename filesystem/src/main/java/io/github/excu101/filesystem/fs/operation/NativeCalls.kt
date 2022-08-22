package io.github.excu101.filesystem.fs.operation

import java.io.FileDescriptor

object NativeCalls {

    init {
        System.loadLibrary("unix-calls")
    }

    external fun putByte(address: Long, data: Byte)

    external fun getByte(address: Long): Byte

    external fun getFileDescriptor(original: FileDescriptor): Int

    external fun close(descriptor: Int)

    external fun pointerRead(descriptor: Int, address: Long, length: Long, position: Long): Int

}