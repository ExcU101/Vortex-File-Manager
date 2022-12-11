package io.github.excu101.vortex.provider.storage

import io.github.excu101.filesystem.fs.DirectoryObserver
import io.github.excu101.filesystem.fs.observer.PathObservableEventType
import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.observer.PathObservableKey.*
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.service
import io.github.excu101.vortex.data.PathItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class StorageDirectoryObserver constructor(
    directory: Path,
    context: CoroutineContext = IO,
    capacity: Int = 0,
    vararg types: PathObservableEventType,
) : DirectoryObserver {

    constructor(
        directory: PathItem,
        context: CoroutineContext = IO,
        capacity: Int = 0,
        vararg types: PathObservableEventType,
    ) : this(
        directory = directory.value,
        context = context,
        capacity = capacity,
        types = types
    )

    private val scope = CoroutineScope(context)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events = scope.produce(capacity = capacity) {
        val service = directory.service(*types)
        service.register(directory)

        while (true) {
            val key: PathObservableKey = service.take() ?: break
            for (event in key.pollEvents()) {
                send(event)
            }

            if (!key.reset()) {
                key.cancel()
                close()
                break
            }
        }
    }
    override val events: Flow<Event>
        get() = _events.receiveAsFlow()

}

