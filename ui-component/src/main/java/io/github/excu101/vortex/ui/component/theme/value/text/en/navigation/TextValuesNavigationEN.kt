package io.github.excu101.vortex.ui.component.theme.value.text.en.navigation

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexBookmarksActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexFileManagerActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexGroupTitleKey

fun initNavigationValuesEN() {
    Theme[navigationVortexGroupTitleKey] = Text(value = "Vortex")
    Theme[navigationVortexFileManagerActionTitleKey] = Text(value = "File manager")
    Theme[navigationVortexBookmarksActionTitleKey] = Text(value = "Bookmarks")
}