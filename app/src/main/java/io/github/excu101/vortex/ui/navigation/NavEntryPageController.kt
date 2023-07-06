package io.github.excu101.vortex.ui.navigation

import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.menu.MenuActionListener

interface NavEntryPageController : MenuActionListener {

    val isInSelectionMode: Boolean

    fun getNavigationIconType(): Int

    fun getNavigationActions(): List<MenuAction>

    fun onEnterSelectionMode()

    fun onLeaveSelectionMode()

}