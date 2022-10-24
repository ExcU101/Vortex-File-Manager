package io.github.excu101.vortex.ui.component.warning

import io.github.excu101.pluginsystem.model.Action

fun interface WarningActionListener {

    fun onWarningActionCall(action: Action)

}