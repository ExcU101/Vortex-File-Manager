package io.github.excu101.vortex.data.storage.impl

import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemPartInfoParser

class LastModifiedTimePartInfoParser : PathItemPartInfoParser {

    override fun getPartInfo(item: PathItem): String? = if (item.isDirectory) {
        null
    } else {
        item.lastModifiedTime.toString()
    }

}