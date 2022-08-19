package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.model.GroupAction

interface GroupManager : Register<GroupAction> {
    val groups: List<PluginPair<GroupAction>>
}