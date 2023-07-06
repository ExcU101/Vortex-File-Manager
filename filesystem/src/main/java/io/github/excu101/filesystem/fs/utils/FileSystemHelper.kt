package io.github.excu101.filesystem.fs.utils

import io.github.excu101.filesystem.fs.path.Path
import kotlinx.coroutines.flow.Flow

abstract class FileSystemHelper {

    abstract fun getCount(path: Path): Int

    abstract fun getFileCount(path: Path): Int

    abstract fun getDirectoryCount(path: Path): Int

    abstract fun getDirectorySize(path: Path): Long

    abstract fun getDirectoryFlowSize(path: Path): Flow<Long>

}