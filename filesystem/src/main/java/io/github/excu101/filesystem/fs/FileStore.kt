package io.github.excu101.filesystem.fs

abstract class FileStore {

    abstract val totalSpace: Long

    abstract val usableSpace: Long

    abstract val unallocatedSpace: Long

}