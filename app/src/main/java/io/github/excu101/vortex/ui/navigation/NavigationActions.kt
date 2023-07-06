package io.github.excu101.vortex.ui.navigation

import io.github.excu101.vortex.theme.ThemeText
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.ui.component.drawer.ItemBottomDrawerFragment
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.dsl.withGroup
import io.github.excu101.vortex.ui.component.item.divider.divider
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.theme.key.text.navigation.navigationPluginGroupTitleKey
import io.github.excu101.vortex.theme.key.text.navigation.navigationPluginManagerActionTitleKey
import io.github.excu101.vortex.theme.key.text.navigation.navigationVortexBookmarksActionTitleKey
import io.github.excu101.vortex.theme.key.text.navigation.navigationVortexFileManagerActionTitleKey
import io.github.excu101.vortex.theme.key.text.navigation.navigationVortexGroupTitleKey
import io.github.excu101.vortex.ui.icon.Icons

object NavigationActions : ItemBottomDrawerFragment.ItemsProvider {

    override val items: List<Item<*>> = scope {
        withGroup(io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.text.navigation.navigationVortexGroupTitleKey)) {
            drawerItem {
                id = ViewIds.Navigation.Menu.FileManagerId
                title =
                    io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.text.navigation.navigationVortexFileManagerActionTitleKey)
                icon = Icons.Rounded.Folder
            }
            drawerItem {
                id = ViewIds.Navigation.Menu.BookmarksId
                title =
                    io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.text.navigation.navigationVortexBookmarksActionTitleKey)
                icon = Icons.Rounded.Bookmark
            }
        }

        divider()

        withGroup(io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.text.navigation.navigationPluginGroupTitleKey)) {
            drawerItem {
                title =
                    io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.text.navigation.navigationPluginManagerActionTitleKey)
                icon = Icons.Rounded.AddBox
            }
        }

        divider()

        withGroup("Additional") {
            drawerItem {
                title = "Switch theme"
                icon = Icons.Rounded.DarkMode
            }
            drawerItem {
                id = ViewIds.Navigation.Menu.SettingsId
                title = "Settings"
                icon = Icons.Rounded.Settings
            }
        }
    }

}