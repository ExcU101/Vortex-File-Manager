package io.github.excu101.vortex.provider

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction

abstract class ActionProvider {

    // Bar action
    abstract fun defaultBarActions(): List<Action>

    // Drawer actions
    abstract fun defaultDrawerGroups(isDark: Boolean): List<GroupAction>

}