package io.github.excu101.vortex.ui.component.theme.value.text.en.navigation

import io.github.excu101.manager.model.Text
import io.github.excu101.manager.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationPluginGroupTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationPluginManagerActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexBookmarksActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexFileManagerActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexGroupTitleKey

fun initNavigationValuesEN() {
    Theme[navigationVortexGroupTitleKey] = Text(value = "Vortex")
    Theme[navigationVortexFileManagerActionTitleKey] = Text(value = "File manager")
    Theme[navigationVortexBookmarksActionTitleKey] = Text(value = "Bookmarks")

    Theme[navigationPluginGroupTitleKey] = Text(value = "Plugin")
    Theme[navigationPluginManagerActionTitleKey] = Text(value = "Plugin Manager")
}