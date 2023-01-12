package io.github.excu101.vortex.ui.screen.settings

import io.github.excu101.pluginsystem.model.Color
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.item.text.text
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.icon.Icons

object SettingPageScreen {

    data class State(
        val content: List<Item<*>> = scope {
            text(
                value = "Choose option to configure for your options"
            ) {
                color = Color.Gray.value
            }
            drawerItem {
                id = ViewIds.Settings.Options.BehaviorId
                title = "Notification"
                icon = Icons.Rounded.Notifications
            }
            drawerItem {
                id = ViewIds.Settings.Options.AppearanceId
                title = "Appearance"
                icon = Icons.Rounded.Palette
            }
            drawerItem {
                id = ViewIds.Settings.Options.BehaviorId
                title = "Behavior"
                icon = Icons.Rounded.Build
            }
        },
        val actions: List<MenuAction> = listOf(
            MenuAction(
                id = ViewIds.Settings.Menu.SearchId,
                title = "Search settings item",
                icon = Icons.Rounded.Search
            )
        )
    )

    class SideEffect

}