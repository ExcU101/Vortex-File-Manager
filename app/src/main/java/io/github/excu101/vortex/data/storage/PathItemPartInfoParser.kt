package io.github.excu101.vortex.data.storage

import io.github.excu101.vortex.data.PathItem

fun interface PathItemPartInfoParser {

    fun getPartInfo(item: PathItem): String?

}