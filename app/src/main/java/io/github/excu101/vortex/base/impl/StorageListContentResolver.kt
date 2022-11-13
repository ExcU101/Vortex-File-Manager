package io.github.excu101.vortex.base.impl

import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.fileListDirectoriesCountSectionKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilesCountSectionKey

typealias Sorter<T> = Comparator<T>

fun interface Filter<T> {
    fun accept(item: T): Boolean
}

enum class Order {
    ASCENDING,
    DESCENDING
}

interface ResultParser<T> {
    fun parse(content: List<T>): List<Item<*>>
}

internal class ResultParserImpl : ResultParser<PathItem> {
    override fun parse(content: List<PathItem>): List<Item<*>> {
        val result = mutableListOf<Item<*>>().apply {
            addAll(elements = content)
        }

        var offset = 0
        val directories = content.count { it.isDirectory }
        val files = content.count { it.isFile }
        val links = content.count { it.isLink }

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