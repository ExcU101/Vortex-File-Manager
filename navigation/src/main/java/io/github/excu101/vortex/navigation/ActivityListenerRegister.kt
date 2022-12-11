package io.github.excu101.vortex.navigation

interface ActivityListenerRegister {

    fun addListener(listener: ActivityListener)

    fun removeListener(listener: ActivityListener)

}