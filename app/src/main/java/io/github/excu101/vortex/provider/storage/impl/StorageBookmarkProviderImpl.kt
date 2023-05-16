package io.github.excu101.vortex.provider.storage.impl

import io.github.excu101.vortex.base.utils.new
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class StorageBookmarkProviderImpl @Inject constructor(

) : StorageBookmarkProvider {

    private val _bookmarks = MutableStateFlow<Set<PathItem>>(setOf())
    override val bookmarks: StateFlow<Set<PathItem>>
        get() = _bookmarks.asStateFlow()

    override suspend fun register(path: PathItem) = _bookmarks.new {
        this + path
    }

    override suspend fun unregister(path: PathItem) = _bookmarks.new {
        this - path
    }

}