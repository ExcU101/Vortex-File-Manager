package io.github.excu101.pluginsystem.observer

import io.github.excu101.pluginsystem.model.plugin.PluginState

interface PluginStateObserver {

    fun onChange(state: PluginState)

}