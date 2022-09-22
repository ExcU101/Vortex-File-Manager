package io.github.excu101.vortex.data.storage

import io.github.excu101.vortex.base.impl.Sorter
import io.github.excu101.vortex.data.PathItem

object PathItemSorters {

    val Name: Sorter<PathItem> = compareBy { item ->
        item.name
    }

    val Path: Sorter<PathItem> = compareBy { item ->
        item.value
    }

    val Size: Sorter<PathItem> = compareBy { item ->
        item.size.memory
    }

    val LastModifiedTime: Sorter<PathItem> = compareBy { item ->
        item.lastModifiedTime.toNanos()
    }

    val LastAccessTime: Sorter<PathItem> = compareBy { item ->
        item.lastAccessTime.toNanos()
    }

    val CreationTime: Sorter<PathItem> = compareBy { item ->
        item.creationTime.toNanos()
    }

}