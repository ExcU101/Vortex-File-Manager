package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.provider.impl.ReflectionPluginFactory

object Providers {

    object Plugin {
        val ReflectionPluginFactory: PluginFactory = ReflectionPluginFactory()
    }

}