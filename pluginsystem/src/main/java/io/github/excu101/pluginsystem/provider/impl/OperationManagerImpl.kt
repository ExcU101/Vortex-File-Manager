package io.github.excu101.pluginsystem.provider.impl

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.pluginsystem.error.PluginNotActivated
import io.github.excu101.pluginsystem.model.Plugin
import io.github.excu101.pluginsystem.provider.OperationManager
import io.github.excu101.pluginsystem.provider.PluginPair
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

internal class OperationManagerImpl : OperationManager {

    private val _operations: MutableList<PluginPair<KClass<out FileOperation>>> = mutableListOf()
    override val availableOperations: List<PluginPair<KClass<out FileOperation>>>
        get() = _operations

    fun provide(plugin: Plugin, vararg args: Any?): FileOperation? {
        return _operations.find {
            it.first == plugin
        }?.second?.primaryConstructor?.call(args)
    }

    override fun register(plugin: Plugin, value: KClass<out FileOperation>) {
        _operations.add(plugin to value)
    }

    override fun unregister(plugin: Plugin) {
        _operations.removeAll { plugin == it.first }
    }

    fun getPlugin(operation: KClass<out FileOperation>): Plugin {
        var plugin: Plugin? = null
        _operations.forEach { (p, o) ->
            if (operation == o) {
                plugin = p
            }
        }
        return plugin ?: throw PluginNotActivated()
    }

}