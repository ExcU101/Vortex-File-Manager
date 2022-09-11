package io.github.excu101.vortex.ui.component.list.adapter.section

import io.github.excu101.vortex.ui.component.list.adapter.listener.SelectionListenerRegister

interface SelectionOwner<T> : SelectionListenerRegister<T> {

    val selectionMode: Mode

    enum class Mode {
        SINGLE,
        MULTIPLE
    }

}