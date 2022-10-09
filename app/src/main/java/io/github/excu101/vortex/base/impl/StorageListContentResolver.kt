package io.github.excu101.vortex.base.impl

import io.github.excu101.pluginsystem.ui.theme.ReplacerThemeText
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.header.text.TextHeaderItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.fileListDirectoriesCountSectionKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilesCountSectionKey
import io.github.excu101.vortex.ui.component.theme.key.specialSymbol

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

        val directories = content.count { it.isDirectory }
        val files = content.count { it.isFile }

        if (directories > 0) {
            val dirPoint = content.indexOfFirst { it.isDirectory }

            result.add(
                index = dirPoint,
                element = TextHeaderItem(
                    value = ReplacerThemeText(
                        key = fileListDirectoriesCountSectionKey,
                        old = specialSymbol,
                        new = directories.toString()
                    )
                )
            )
        }

        if (files > 0) {
            val filePoint = content.indexOfFirst { it.isFile }

            result.add(
                index = filePoint,
                element = TextHeaderItem(
                    value = ReplacerThemeText(
                        key = fileListFilesCountSectionKey,
                        old = specialSymbol,
                        new = files.toString()
                    )
                )
            )
        }
        return result
    }
}