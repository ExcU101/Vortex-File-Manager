package io.github.excu101.vortex.ui.component.list.adapter.selection

import io.github.excu101.vortex.ui.component.list.adapter.EditableAdapter

interface SelectableAdapter<T> : EditableAdapter<T> {

    fun select(item: T)

    fun isSelected(position: Int): Boolean

    fun replaceSelected(selected: List<T>)

}

fun <T> SelectableAdapter<T>.replaceSelected(vararg selected: T) =
    replaceSelected(selected.toList())

fun <T> SelectableAdapter<T>.replaceSelected(item: T) = replaceSelected(listOf(item))