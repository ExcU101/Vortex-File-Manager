package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.model.plugin.Plugin

interface PluginFactory {

    fun create(pluginClass: Class<out Plugin>): Plugin?

}