package io.github.excu101.pluginsystem.model.plugin

interface PluginDescriptor {

    val id: String

    val description: PluginDescription

    val pluginClass: String

    val dependencies: List<PluginDependency>

    val version: String

    val license: String?

    interface Owner {
        val descriptor: PluginDescriptor
    }

}