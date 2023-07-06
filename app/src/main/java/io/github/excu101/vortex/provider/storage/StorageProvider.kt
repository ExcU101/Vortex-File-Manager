package io.github.excu101.vortex.provider.storage

import io.github.excu101.filesystem.fs.PathObserver
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.data.PathItem
import kotlinx.coroutines.flow.Flow

interface StorageProvider {

    suspend fun getItems(item: PathItem): Flow<PathItem>

    fun registerObserver(
        path: Path,
        types: Int,
    ): PathObserver

    fun hasObserver(path: Path): Boolean

    fun closeObserver(
        path: Path,
    )

    fun closeObservers()

    fun copyPath(path: Path)
}