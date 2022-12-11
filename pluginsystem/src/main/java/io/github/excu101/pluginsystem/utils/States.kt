@file:Suppress("FunctionName")

package io.github.excu101.pluginsystem.utils

import io.github.excu101.pluginsystem.model.plugin.PluginCreatedState
import io.github.excu101.pluginsystem.model.plugin.PluginStartedState
import io.github.excu101.pluginsystem.model.plugin.PluginState
import io.github.excu101.pluginsystem.model.plugin.PluginStoppedState

fun PluginCreatedState(): PluginState = object : PluginCreatedState {}
fun PluginStoppedState(): PluginState = object : PluginStoppedState {}
fun PluginStartedState(): PluginState = object : PluginStartedState {}