package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.model.Plugin

interface Register<T> {

    fun register(plugin: Plugin, value: T)

    fun unregister(plugin: Plugin)

}

typealias PluginPair<T> = Pair<Plugin, T>

operator fun <T> Register<T>.set(plugin: Plugin, value: T) = register(plugin, value)