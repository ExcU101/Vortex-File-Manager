package io.github.excu101.filesystem.fs

abstract class FileStore {

    abstract val name: String

    abstract val type: String

    abstract val totalSpace: Long

    abstract val usableSpace: Long

    abstract val unallocatedSpace: Long

    abstract val blockSize: Long

}