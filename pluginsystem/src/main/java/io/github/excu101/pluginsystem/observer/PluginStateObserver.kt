package io.github.excu101.pluginsystem.observer

interface PluginStateObserver {

    object States {
        const val Started = 1
        const val Created = 2
        const val Stopped = 3
    }

    fun onChange(state: Int)

}