package io.github.excu101.pluginsystem.provider

import io.github.excu101.filesystem.fs.operation.FileOperation
import kotlin.reflect.KClass

interface OperationManager : Register<KClass<out FileOperation>> {
    val availableOperations: List<PluginPair<KClass<out FileOperation>>>
}