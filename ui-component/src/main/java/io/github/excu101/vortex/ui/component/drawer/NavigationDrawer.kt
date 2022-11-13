package io.github.excu101.vortex.ui.component.drawer

import io.github.excu101.vortex.ui.component.list.adapter.Item

interface NavigationDrawer : Drawer {

    fun registerListener(listener: DrawerActionListener)

    fun replaceNavigation(items: List<Item<*>>)

    fun replaceSelected(items: List<Item<*>>)

}