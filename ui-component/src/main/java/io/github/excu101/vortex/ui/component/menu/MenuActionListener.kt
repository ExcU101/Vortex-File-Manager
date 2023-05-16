package io.github.excu101.vortex.ui.component.menu

fun interface MenuActionListener {
    fun onMenuActionCall(action: MenuAction): Boolean
}