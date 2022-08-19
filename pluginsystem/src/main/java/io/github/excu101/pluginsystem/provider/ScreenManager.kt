package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.model.Screen

interface ScreenManager : Register<Screen> {

    val screens: List<PluginPair<Screen>>

}