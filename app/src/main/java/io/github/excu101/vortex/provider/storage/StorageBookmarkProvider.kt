package io.github.excu101.vortex.provider.storage

import io.github.excu101.vortex.data.PathItem

object StorageBookmarkProvider {

    private val _items = HashSet<PathItem>()
    val items: Set<PathItem>
        get() = _items

    fun register(item: PathItem): Boolean {
        return _items.add(item)
    }

    fun unregister(item: PathItem): Boolean {
        return _items.remove(item)
    }

}