package io.github.excu101.vortex.ui.component.list.adapter.listener

fun interface ItemViewSelectionListener<T> {

    fun onItemSelectionChanged(item: T, isSelected: Boolean)

}