package io.github.excu101.pluginsystem

interface PluginSystemConfiguration {
    val pluginDirectory: String

    object EmptyPluginSystemConfiguration : PluginSystemConfiguration {
        override val pluginDirectory: String = ""
    }
}