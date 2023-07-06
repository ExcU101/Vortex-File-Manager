package io.github.excu101.vortex.provider.storage

import io.github.excu101.vortex.ui.component.list.adapter.Item

typealias Sorter<T> = Comparator<T>

fun interface Filter<T> {
    fun accept(item: T): Boolean

    operator fun get(item: T) = accept(item)

}

enum class Order {
    ASCENDING,
    DESCENDING
}

enum class View {
    COLUMN,
    GRID
}

interface ResultParser<T> {
    fun parse(content: List<T>): List<Item<*>>
}