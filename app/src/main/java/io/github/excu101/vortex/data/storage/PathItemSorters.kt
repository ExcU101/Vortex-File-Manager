package io.github.excu101.vortex.data.storage

import io.github.excu101.vortex.base.impl.Sorter
import io.github.excu101.vortex.data.PathItem

object PathItemSorters {

    val Name: Sorter<PathItem> = compareByDescending<PathItem> { item ->
        item.isDirectory
    }.thenBy {
        it.name
    }

    val Path: Sorter<PathItem> = compareByDescending<PathItem> { item ->
        item.isDirectory
    }.thenBy {
        it.value
    }

    val Size: Sorter<PathItem> = compareByDescending<PathItem> { item ->
        item.isDirectory
    }.thenBy {
        it.size.memory
    }

    val LastModifiedTime: Sorter<PathItem> = compareByDescending<PathItem> { item ->
        item.isDirectory
    }.thenBy {
        it.lastModifiedTime.nanos
    }

    val LastAccessTime: Sorter<PathItem> = compareByDescending<PathItem> { item ->
        item.isDirectory
    }.thenBy {
        it.lastAccessTime.nanos
    }

    val CreationTime: Sorter<PathItem> = compareByDescending<PathItem> { item ->
        item.isDirectory
    }.thenBy {
        it.creationTime.nanos
    }

}