package io.github.excu101.vortex.ui.component.list.adapter.listener

interface ClickListenerRegister<T> {

    fun register(listener: ItemViewListener<T>)

    fun registerLong(listener: ItemViewLongListener<T>)

}