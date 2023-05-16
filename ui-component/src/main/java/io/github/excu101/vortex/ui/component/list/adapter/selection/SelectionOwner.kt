package io.github.excu101.vortex.ui.component.list.adapter.selection

import io.github.excu101.vortex.ui.component.list.adapter.listener.SelectionListenerRegister

interface SelectionOwner<T> : SelectionListenerRegister<T> {

    val selection: SelectionHandler<T>

}