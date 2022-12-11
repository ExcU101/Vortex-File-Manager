package io.github.excu101.vortex.ui.screen.navigation

import io.github.excu101.vortex.navigation.ActivityListener
import io.github.excu101.vortex.navigation.ActivityListenerRegister

class NavigationActivityProvider : ActivityListenerRegister {

    private val listeners = mutableListOf<ActivityListener>()

    override fun addListener(listener: ActivityListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: ActivityListener) {
        listeners.remove(listener)
    }

}