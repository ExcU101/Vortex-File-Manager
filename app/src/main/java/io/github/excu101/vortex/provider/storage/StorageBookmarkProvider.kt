package io.github.excu101.vortex.provider.storage

import io.github.excu101.vortex.data.PathItem
import kotlinx.coroutines.flow.StateFlow

interface StorageBookmarkProvider {

    val bookmarks: StateFlow<Set<PathItem>>

    suspend fun register(path: PathItem)

    suspend fun unregister(path: PathItem)

}