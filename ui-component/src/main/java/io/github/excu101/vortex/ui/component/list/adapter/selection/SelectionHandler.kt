package io.github.excu101.vortex.ui.component.list.adapter.selection

interface SelectionHandler<T> {
    val selected: T
    val count: Int
    var isPayload: Boolean

    fun isSelected(position: Int): Boolean
    fun isSelected(item: T): Boolean
    fun updateSelected(item: T)
}
