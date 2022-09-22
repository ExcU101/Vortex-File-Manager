package io.github.excu101.pluginsystem.model

import io.github.excu101.filesystem.fs.operation.FileOperation
import kotlin.reflect.KClass

data class PluginOperation(
    val name: String,
    val src: KClass<out FileOperation>,
)