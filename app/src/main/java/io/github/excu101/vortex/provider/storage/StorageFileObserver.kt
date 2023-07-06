package io.github.excu101.vortex.provider.storage

import io.github.excu101.filesystem.fs.PathObserver
import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.observer.PathObservableKey.*
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.service
import io.github.excu101.vortex.data.PathItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class StorageFileObserver constructor(
    path: Path,
    val context: CoroutineContext = newSingleThreadContext(name = "file-observer"),
    capacity: Int = 0,
    types: Int,
) : PathObserver {

    constructor(
        path: PathItem,
        context: CoroutineContext = newSingleThreadContext(name = "file-observer"),
        capacity: Int = 0,
        types: Int,
    ) : this(
        path = path.value,
        context = context,
        capacity = capacity,
        types = types
    )

    override fun cancel(cause: CancellationException?) {
        _events.cancel(cause)
        context.cancel()
    }

    private val scope = CoroutineScope(context)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events = scope.produce(capacity = capacity) {
        val service = path.service(types)

        while (true) {
            val key: PathObservableKey = service.take() ?: break
            for (event in key.pollEvents()) send(event)

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

