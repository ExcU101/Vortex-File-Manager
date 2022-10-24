package io.github.excu101.vortex.data.storage.impl

import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemPartInfoParser
import io.github.excu101.vortex.utils.convertToThemeText

class SizePartInfoParser : PathItemPartInfoParser {
    override fun getPartInfo(item: PathItem): String? {
        return if (item.isDirectory) null else item.size.convertToThemeText()
    }
}