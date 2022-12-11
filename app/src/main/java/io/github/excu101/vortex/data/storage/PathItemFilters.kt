package io.github.excu101.vortex.data.storage

import io.github.excu101.filesystem.fs.utils.*
import io.github.excu101.vortex.base.impl.Filter
import io.github.excu101.vortex.data.PathItem

object PathItemFilters {

    val Empty = Filter<PathItem> { true }

    val OnlyFolder = Filter<PathItem> { item ->
        item.isDirectory
    }

    val OnlyFile = Filter<PathItem> { item ->
        item.isFile
    }

    val OnlyTextFile = Filter<PathItem> { item ->
        item.mime.isTextType
    }

    val OnlyVideoFile = Filter<PathItem> { item ->
        item.mime.isVideoType
    }

    val OnlyAudioFile = Filter<PathItem> { item ->
        item.mime.isAudioType
    }

    val OnlyApplicationFile = Filter<PathItem> { item ->
        item.mime.isApplicationType
    }

    val OnlyImageFile = Filter<PathItem> { item ->
        item.mime.isImageType
    }

}