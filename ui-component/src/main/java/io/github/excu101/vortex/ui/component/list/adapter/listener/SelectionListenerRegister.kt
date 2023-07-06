package io.github.excu101.vortex.ui.component.list.adapter.listener

interface SelectionListenerRegister<T> {

    fun registerItemSelection(listener: ItemViewSelectionListener<T>)

    fun registerSelection(listener: SelectionListener<T>)

}