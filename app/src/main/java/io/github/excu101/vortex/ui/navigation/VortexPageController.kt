package io.github.excu101.vortex.ui.navigation

import android.content.Context
import io.github.excu101.vortex.navigation.page.PageController

abstract class VortexPageController(context: Context) : PageController(context) {

    override val controller: VortexNavigationController?
        get() = super.controller as? VortexNavigationController?

}