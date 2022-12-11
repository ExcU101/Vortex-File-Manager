package io.github.excu101.pluginsystem.provider.impl

import io.github.excu101.pluginsystem.model.plugin.Plugin
import io.github.excu101.pluginsystem.provider.PluginFactory
import io.github.excu101.pluginsystem.provider.Providers
import java.lang.reflect.Modifier

class ReflectionPluginFactory : PluginFactory {
    override fun create(pluginClass: Class<out Plugin>): Plugin? {
        val modifies = pluginClass.modifiers
        require(!Modifier.isAbstract(modifies) || !Modifier.isInterface(modifies)) {
            "Plugin must be non-abstracted"
        }

        return try {
            val constructor = pluginClass.getConstructor()
            constructor.newInstance()
        } catch (exception: Exception) {
            null
        }
    }
}