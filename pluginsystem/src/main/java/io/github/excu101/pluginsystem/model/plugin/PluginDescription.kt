package io.github.excu101.pluginsystem.model.plugin

interface PluginDescription {
    val original: String
    val tags: List<String>
    val count: Int
}