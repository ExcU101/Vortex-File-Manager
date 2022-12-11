package io.github.excu101.pluginsystem.provider

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.pluginsystem.model.plugin.Plugin

internal object PluginManagerImpl {

    const val pluginClassName = "io.github.excu101.pluginsystem.model.plugin.Plugin"
    const val pluginExtensionName = "jar"

    private val _plugins: MutableSet<Plugin> = mutableSetOf()
    val plugins: List<Plugin>
        get() = _plugins.toList()

    val loadedPlugins: List<Plugin>
        get() = listOf()

    fun loadPlugin(path: Path): String {
        return ""
    }

    fun loadPlugins() {

    }


}