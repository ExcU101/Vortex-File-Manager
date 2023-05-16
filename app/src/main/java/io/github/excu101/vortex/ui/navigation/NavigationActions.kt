package io.github.excu101.vortex.ui.navigation

import io.github.excu101.manager.ui.theme.ThemeText
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.ui.component.drawer.ItemBottomDrawerFragment
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.dsl.withGroup
import io.github.excu101.vortex.ui.component.item.divider.divider
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationPluginGroupTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationPluginManagerActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexBookmarksActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexFileManagerActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexGroupTitleKey
import io.github.excu101.vortex.ui.icon.Icons

object NavigationActions : ItemBottomDrawerFragment.ItemsProvider {

    override val items: List<Item<*>> = scope {
        withGroup(ThemeText(navigationVortexGroupTitleKey)) {
            drawerItem {
                id = ViewIds.Navigation.Menu.FileManagerId
                title = ThemeText(navigationVortexFileManagerActionTitleKey)
                icon = Icons.Rounded.Folder
            }
            drawerItem {
                id = ViewIds.Navigation.Menu.BookmarksId
                title = ThemeText(navigationVortexBookmarksActionTitleKey)
                icon = Icons.Rounded.Bookmark
            }
        }

        divider()

        withGroup(ThemeText(navigationPluginGroupTitleKey)) {
            drawerItem {
                title = ThemeText(navigationPluginManagerActionTitleKey)
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