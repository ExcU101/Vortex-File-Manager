package io.github.excu101.vortex.ui.component.drawer

import io.github.excu101.pluginsystem.model.Action

fun interface DrawerActionListener {

    fun onDrawerActionCall(action: Action)

}