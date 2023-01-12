package io.github.excu101.vortex.provider.storage.impl

import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.ResultParser
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.fileListDirectoriesCountSectionKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilesCountSectionKey

internal class ListResultParser : ResultParser<PathItem> {
    override fun parse(content: List<PathItem>): List<Item<*>> {
        val result = mutableListOf<Item<*>>().apply {
            addAll(elements = content)
        }

        var offset = 0
        val directories = content.count { it.isDirectory }
        val files = content.count { it.isFile }
        val links = content.count { it.isLink }

        directories > files // Directories first
        files > directories // Files first

        if (directories > 0) {
            val dirPoint = content.indexOfFirst { it.isDirectory } + offset

            result.add(
                index = dirPoint,
                element = TextItem(
                    value = FormatterThemeText(
                        key = fileListDirectoriesCountSectionKey,
                        directories
                    ),
                )
            )

            offset++
        }

        if (files > 0) {

            val filePoint = content.indexOfFirst { it.isFile } + offset

            result.add(
                index = filePoint,
                element = TextItem(
                    value = FormatterThemeText(
                        key = fileListFilesCountSectionKey,
                        files
                    ),
                )
            )

            offset++
        }

        if (links > 0) {
            val linkPoint = content.indexOfFirst { it.isLink } + offset

            result.add(
                index = linkPoint,
                element = TextItem(
                    value = FormatterThemeText(
                        key = "Link",
                        links
                    )
                )
            )

            offset++
        }

        return result
    }
}