package io.github.excu101.vortex.ui.navigation

import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.dsl.withDividers
import io.github.excu101.vortex.ui.component.dsl.withGroup
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexBookmarksActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexFileManagerActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexGroupTitleKey
import io.github.excu101.vortex.ui.icon.Icons

class NavigationActions {

    val actions: List<Item<*>>
        get() = scope {
            withDividers {
                withGroup(ThemeText(navigationVortexGroupTitleKey)) {
                    drawerItem {
                        title = ThemeText(navigationVortexFileManagerActionTitleKey)
                        icon = Icons.Rounded.Folder
                    }
                    drawerItem {
                        title = ThemeText(navigationVortexBookmarksActionTitleKey)
                        icon = Icons.Rounded.Bookmark
                    }
                }
                withGroup("Additional") {
                    drawerItem {
                        title = "Switch theme"
                        icon = Icons.Rounded.DarkMode
                    }
                    drawerItem {
                        title = "Settings"
                        icon = Icons.Rounded.Settings
                    }
                }
            }
        }

}