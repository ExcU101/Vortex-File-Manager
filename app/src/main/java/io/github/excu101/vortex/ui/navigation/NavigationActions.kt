package io.github.excu101.vortex.ui.navigation

import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.NavigationDrawerHeaderItem
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.divider.divider
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.item.text.text
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexBookmarksActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexFileManagerActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexGroupTitleKey

class NavigationActions(
    private val resources: ResourceProvider,
) {

    val actions: List<Item<*>>
        get() = actions()

    private fun actions() = scope {
        add(NavigationDrawerHeaderItem)
        divider()
        text(value = ThemeText(navigationVortexGroupTitleKey))
        drawerItem {
            title = ThemeText(navigationVortexFileManagerActionTitleKey)
            icon = resources[R.drawable.ic_folder_24]
        }
        drawerItem {
            title = ThemeText(navigationVortexBookmarksActionTitleKey)
            icon = resources[R.drawable.ic_bookmark_24]
        }
    }

}