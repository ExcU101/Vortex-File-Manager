package io.github.excu101.pluginsystem.provider

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.pluginsystem.model.Plugin

interface PluginManager {

    val plugins: List<Plugin>

    val loadedPlugins: List<Plugin>

    fun loadPlugins()

    fun loadPlugin(path: Path): String

}