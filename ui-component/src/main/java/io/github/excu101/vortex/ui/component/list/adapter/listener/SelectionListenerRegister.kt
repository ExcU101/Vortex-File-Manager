package io.github.excu101.vortex.ui.component.list.adapter.listener

fun interface SelectionListenerRegister<T> {

    fun registerSelection(listener: ItemViewSelectionListener<T>)

}