package io.github.excu101.vortex.ui.component.adapter.section

import io.github.excu101.vortex.ui.component.adapter.listener.SelectionListenerRegister

interface SelectionOwner<T> : SelectionListenerRegister<T> {

    val selectionMode: Mode

    enum class Mode {
        SINGLE,
        MULTIPLE
    }

}