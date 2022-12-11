package io.github.excu101.filesystem.fs.observer

import io.github.excu101.filesystem.fs.path.Path
import kotlinx.coroutines.flow.SharedFlow

interface PathObserverService {

    val isOpen: Boolean

    suspend fun register(
        source: Path,
        vararg types: PathObservableEventType,
    ): PathObservableKey

    suspend fun take(): PathObservableKey?

    fun close()

}