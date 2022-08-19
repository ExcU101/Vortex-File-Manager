package io.github.excu101.pluginsystem.provider.impl

import io.github.excu101.pluginsystem.error.PluginNotActivated
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.model.Plugin
import io.github.excu101.pluginsystem.provider.GroupManager
import io.github.excu101.pluginsystem.provider.PluginPair

internal class GroupManagerImpl : GroupManager {

    private val _groups: MutableList<PluginPair<GroupAction>> = mutableListOf()
    override val groups: List<PluginPair<GroupAction>>
        get() = _groups

    override fun register(plugin: Plugin, value: GroupAction) {
        _groups.add(plugin to value)
    }

    override fun unregister(plugin: Plugin) {
        _groups.removeAll { it.first == plugin }
    }

    fun getPlugin(group: GroupAction): Plugin {
        var plugin: Plugin? = null
        _groups.forEach { (p, s) ->
            if (group == s) {
                plugin = p
            }
        }
        return plugin ?: throw PluginNotActivated()
    }
}