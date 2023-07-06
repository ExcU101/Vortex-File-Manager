package io.github.excu101.vortex.data.storage.impl

import io.github.excu101.filesystem.fs.utils.count
import io.github.excu101.vortex.theme.FormatterThemeText
import io.github.excu101.vortex.theme.ThemeText
import io.github.excu101.vortex.data.storage.PathItemPartInfoParser
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemCountKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemEmptyKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemsCountKey

fun ItemCountPartInfoParsers() = PathItemPartInfoParser { item ->
    if (item.isDirectory)
        when (val count = item.value.count) {
            -1 -> io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.text.storage.item.fileListItemEmptyKey)
            0 -> io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.text.storage.item.fileListItemEmptyKey)
            1 -> io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.text.storage.item.fileListItemCountKey)
            else -> io.github.excu101.vortex.theme.FormatterThemeText(
                io.github.excu101.vortex.theme.key.text.storage.item.fileListItemsCountKey,
                count
            )
        }
    else null
}