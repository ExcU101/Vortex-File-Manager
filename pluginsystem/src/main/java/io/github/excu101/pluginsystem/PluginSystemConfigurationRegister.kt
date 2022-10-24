package io.github.excu101.pluginsystem

import io.github.excu101.pluginsystem.PluginSystemConfiguration.EmptyPluginSystemConfiguration

object PluginSystemConfigurationRegister {

    private var _configuration: PluginSystemConfiguration? = null

    val configuration: PluginSystemConfiguration
        get() = _configuration ?: EmptyPluginSystemConfiguration

    fun register(configuration: PluginSystemConfiguration) {
        if (configuration != null) throw IllegalArgumentException("Configuration already setted")

        this._configuration = configuration
    }

}