package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.observer.PathObservableKey
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

interface PathObserver {

    val events: Flow<PathObservableKey.Event>

    fun cancel(cause: CancellationException? = null)

}

suspend fun PathObserver.collect(
    collector: FlowCollector<PathObservableKey.Event> = FlowCollector { },
) {
    events.collect(collector)
}