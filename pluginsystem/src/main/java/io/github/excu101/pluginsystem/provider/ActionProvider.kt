package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction

interface ActionProvider {

    fun barActions(): MutableList<Action>

    fun drawerActions(): MutableList<GroupAction>

}