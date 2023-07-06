package io.github.excu101.vortex.provider.storage.impl

import io.github.excu101.vortex.theme.FormatterThemeText
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.ResultParser
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.theme.key.fileListDirectoriesCountSectionKey
import io.github.excu101.vortex.theme.key.fileListFilesCountSectionKey

private const val FILES = 0
private const val DIRS = 1
private const val LINK = 1

internal class ListResultParser : ResultParser<PathItem> {

    override fun parse(content: List<PathItem>): List<Item<*>> {
        val result = mutableListOf<Item<*>>().apply {
            addAll(elements = content)
        }

        var offset = 0
        var directories = 0
        var files = 0
        var links = 0

        content.forEach { item ->
            when {
                item.isDirectory -> directories++
                item.isFile -> files++
                item.isLink -> links++
            }
        }

        if (directories > 0) {
            val dirPoint = content.indexOfFirst { it.isDirectory } + offset

            result.add(
                index = dirPoint,
                element = TextItem(
                    value = io.github.excu101.vortex.theme.FormatterThemeText(
                        key = io.github.excu101.vortex.theme.key.fileListDirectoriesCountSectionKey,
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
                    value = io.github.excu101.vortex.theme.FormatterThemeText(
                        key = io.github.excu101.vortex.theme.key.fileListFilesCountSectionKey,
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
                    value = io.github.excu101.vortex.theme.FormatterThemeText(
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