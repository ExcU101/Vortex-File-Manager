package io.github.excu101.vortex.ui.component.menu

import io.github.excu101.pluginsystem.model.Action

fun interface ActionListener {
    fun onCall(action: Action)
}