package io.github.excu101.vortex.ui.navigation

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.navigation.NavigationController
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexBookmarksActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexFileManagerActionTitleKey

class ActionNavigator(
    private val controller: NavigationController,
) {

    fun navigate(action: Action, onElse: () -> Unit) {
        when (action.title) {
            ThemeText(navigationVortexFileManagerActionTitleKey) -> {
                controller.navigate(
                    route = AppNavigation.Routes.Storage.List.Page
                )
            }

            ThemeText(navigationVortexBookmarksActionTitleKey) -> {
                controller.navigate(
                    route = AppNavigation.Routes.Storage.Bookmark.Page
                )
            }

            else -> onElse()
        }
    }

}