package io.github.excu101.vortex.ui.component.foundtation

interface ListenerRegister<T : Listener> {

    fun registerListener(listener: T)

    fun unregisterListener(listener: T)

}