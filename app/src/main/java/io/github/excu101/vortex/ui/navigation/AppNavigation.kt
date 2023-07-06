package io.github.excu101.vortex.ui.navigation

import android.content.Context
import io.github.excu101.vortex.navigation.NavigationPageController
import io.github.excu101.vortex.navigation.page.PageController
import io.github.excu101.vortex.ui.screen.navigation.HostNavigationPageController
import io.github.excu101.vortex.ui.screen.navigation.NavigationActivity
import io.github.excu101.vortex.ui.screen.storage.StoragePageController

object Navigation {
    fun Storage(context: NavigationActivity) = StoragePageController(context)
    fun Host(context: NavigationActivity) = HostNavigationPageController(context)

    const val Settings = 0
    const val Storage = 1
    const val Bookmark = 2
    const val Host = 3
}