package io.github.excu101.filesystem.fs.observer

interface PathObservableKey {

    val isActual: Boolean

    suspend fun pollEvents(): List<Event>

    suspend fun reset(): Boolean

    suspend fun cancel()

    interface Event
}