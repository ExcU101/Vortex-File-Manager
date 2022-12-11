package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.observer.PathObservableKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

interface DirectoryObserver {

    val events: Flow<PathObservableKey.Event>

}

suspend fun DirectoryObserver.collect(
    collector: FlowCollector<PathObservableKey.Event> = FlowCollector { },
) {
    events.collect(collector)
}