package io.github.excu101.pluginsystem.provider

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.pluginsystem.model.plugin.Plugin

interface PluginManager {

    val plugins: List<Plugin>

    val loadedPlugins: List<Plugin>

    val unloadedPlugins: List<Plugin>

    fun loadPlugins()

    fun loadPlugin(path: Path): String

    /**
     * Checks if something happened with destination
     *
     * @return true - if detected new or deleted plugin, false - otherwise
     * **/
    fun checkDestination(): Boolean

    val version: String

}