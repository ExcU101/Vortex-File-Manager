package io.github.excu101.vortex.base.impl

import io.github.excu101.pluginsystem.ui.theme.ReplacerThemeText
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.header.TextHeaderItem
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.fileListDirectoriesCountSectionKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilesCountSectionKey
import io.github.excu101.vortex.ui.component.theme.key.specialSymbol

typealias Sorter<T> = Comparator<T>

class StorageListContentParser {

    fun interface Filter<T> {
        fun accept(item: T): Boolean
    }

    interface ResultParser<T> {
        fun parse(content: List<T>): List<Item<*>>
    }

    class ResultParserImpl : ResultParser<PathItem> {
        override fun parse(content: List<PathItem>): List<Item<*>> {
            val result = content.toMutableList() as MutableList<Item<*>>

            val files = content.count { it.isFile }
            val directories = content.count { it.isDirectory }

            if (directories > 0) {
                val dirPoint = content.indexOfFirst { it.isDirectory }

                result.add(
                    dirPoint,
                    TextHeaderItem(
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
                    filePoint,
                    TextHeaderItem(
                        value = ReplacerThemeText(
                            key = fileListFilesCountSectionKey,
                            old = specialSymbol,
                            new = files.toString()
                        )
                    ))
            }

            return result
        }

    }

    fun run(
        content: List<PathItem>,
        sorter: Sorter<PathItem>,
        filter: Filter<PathItem> = PathItemFilters.Empty,
        parser: ResultParser<PathItem> = ResultParserImpl(),
    ): List<Item<*>> {
        val sorted = content.sortedWith(sorter)
        val filtered = sorted.filter(filter::accept)

        return parser.parse(filtered)
    }

}