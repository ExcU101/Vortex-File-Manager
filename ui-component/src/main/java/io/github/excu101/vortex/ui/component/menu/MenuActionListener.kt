package io.github.excu101.vortex.ui.component.menu

import io.github.excu101.pluginsystem.model.Action

fun interface MenuActionListener {
    fun onMenuActionCall(action: Action)
}