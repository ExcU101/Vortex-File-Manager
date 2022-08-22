package io.github.excu101.pluginsystem.provider

import android.content.Context
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.model.Plugin
import io.github.excu101.pluginsystem.provider.impl.GroupManagerImpl
import io.github.excu101.pluginsystem.utils.asPluginsUrl
import java.io.File

object PluginManager {

    const val directoryName: String = "plugins"
    const val pluginClassName = "io.github.excu101.pluginsystem.model.Plugin"
    const val pluginExtensionName = "jar"

    private val _plugins: MutableSet<Plugin> = mutableSetOf()
    val plugins: Set<Plugin>
        get() = _plugins

    fun load(path: String) = load(File(path))

    fun getPlugin(group: GroupAction) {
        (Managers.Group as GroupManagerImpl).getPlugin(group)
    }

    fun checkPluginDirectory(context: Context): Boolean =
        File(context.obbDir, directoryName).exists()

    fun createPluginDirectory(context: Context): Boolean =
        File(context.obbDir, directoryName).mkdirs()

    fun readPlugins(context: Context) {
        load(File(context.obbDir, directoryName))
    }

    fun readOrCreatePluginDirectory(context: Context) {
        if (!checkPluginDirectory(context)) {
            createPluginDirectory(context)
        } else {
            readPlugins(context = context)
        }
    }

    fun load(path: File) {
        javaClass.classLoader?.let { loader ->
            PluginLoader(
                urls = path.asPluginsUrl().toTypedArray(),
                loader
            ).getPlugins().forEach { it?.let { plugin -> activate(plugin) } }
        }
    }

    fun activate(plugin: Plugin) {
        _plugins.add(plugin)
        plugin.activate()
    }

    fun disable(plugin: Plugin) {
        _plugins.remove(plugin)
        plugin.disable()
    }
}