package io.github.excu101.pluginsystem.model.plugin

interface Plugin : PluginDescriptor.Owner {

    fun start()

    fun stop()

    fun delete()

}