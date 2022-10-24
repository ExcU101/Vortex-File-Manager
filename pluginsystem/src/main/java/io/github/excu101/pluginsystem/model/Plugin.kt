package io.github.excu101.pluginsystem.model

inline val Plugin.Attributes.pluginId
    get() = "$packageName:$name:$version"

interface Plugin {

    val attributes: Attributes

    fun activate()

    fun disable()

    // Example: io.github.excu101 (packageName) : plugin-system (name) : 1.0.0 (version)
    // Full example: io.github.excu101:plugin-system:1.0.0 (packageName:name:version)
    interface Attributes {
        val name: String
        val version: String
        val packageName: String
    }

    enum class State {
        ACTIVATED,
        DISABLED,
    }
}