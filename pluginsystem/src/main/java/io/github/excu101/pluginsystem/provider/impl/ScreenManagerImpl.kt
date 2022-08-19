package io.github.excu101.pluginsystem.provider.impl

import io.github.excu101.pluginsystem.error.PluginNotActivated
import io.github.excu101.pluginsystem.model.Plugin
import io.github.excu101.pluginsystem.model.Screen
import io.github.excu101.pluginsystem.provider.PluginPair
import io.github.excu101.pluginsystem.provider.ScreenManager

internal class ScreenManagerImpl : ScreenManager {

    private val _screens = mutableListOf<Pair<Plugin, Screen>>()

    override val screens: List<PluginPair<Screen>>
        get() = _screens

    override fun register(plugin: Plugin, value: Screen) {
        _screens.add(plugin to value)
    }

    override fun unregister(plugin: Plugin) {
        _screens.removeAll { it.first == plugin }
    }

    fun getPlugin(screen: Screen): Plugin {
        var plugin: Plugin? = null
        _screens.forEach { (p, s) ->
            if (screen == s) {
                plugin = p
            }
        }
        return plugin ?: throw PluginNotActivated()
    }
}