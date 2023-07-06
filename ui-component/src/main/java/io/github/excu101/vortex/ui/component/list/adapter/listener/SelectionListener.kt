package io.github.excu101.vortex.ui.component.list.adapter.listener

interface SelectionListener<T> {

    fun onSelectionChanged(selected: List<T>)

}