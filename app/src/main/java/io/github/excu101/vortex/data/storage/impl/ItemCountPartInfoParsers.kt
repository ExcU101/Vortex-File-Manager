package io.github.excu101.vortex.data.storage.impl

import io.github.excu101.filesystem.fs.utils.count
import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemPartInfoParser
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemCountKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemEmptyKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemsCountKey

class ItemCountPartInfoParsers : PathItemPartInfoParser {

    override fun getPartInfo(item: PathItem): String? = if (item.isDirectory) {
        when (val count = item.value.count) {
            -1 -> ThemeText(fileListItemEmptyKey)
            0 -> ThemeText(fileListItemEmptyKey)
            1 -> ThemeText(fileListItemCountKey)
            else -> FormatterThemeText(
                key = fileListItemsCountKey,
                count
            )
        }
    } else {
        null
    }

}