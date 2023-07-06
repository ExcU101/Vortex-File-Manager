package io.github.excu101.filesystem.fs.observer

import io.github.excu101.filesystem.fs.path.Path

interface PathObservableKey {

    val isActual: Boolean

    suspend fun pollEvents(): List<Event>

    suspend fun reset(): Boolean

    suspend fun cancel()

    data class Event(val type: Int, val path: Path)
}