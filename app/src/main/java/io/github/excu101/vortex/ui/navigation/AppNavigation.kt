package io.github.excu101.vortex.ui.navigation

import android.content.Context
import io.github.excu101.vortex.navigation.NavPageControllerGraph
import io.github.excu101.vortex.ui.screen.settings.SettingsPageController
import io.github.excu101.vortex.ui.screen.storage.StoragePageController

object Routes {
    const val Settings = 0
    const val Storage = 1
    const val Bookmark = 2
}

val Context.graph: NavPageControllerGraph
    get() = NavPageControllerGraph(context = this)
        .addDestination(Routes.Storage, ::StoragePageController)
        .addDestination(Routes.Settings, ::SettingsPageController)