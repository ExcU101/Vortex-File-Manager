package io.github.excu101.vortex.data.storage

import io.github.excu101.vortex.base.impl.StorageListContentParser.Filter
import io.github.excu101.vortex.data.PathItem

object PathItemFilters {

    val Empty = Filter<PathItem> { true }

    val OnlyFolder = Filter<PathItem> { item ->
        item.isDirectory
    }

    val OnlyFile = Filter<PathItem> { item ->
        item.isFile
    }

}