package io.github.excu101.vortex.service.storage

import io.github.excu101.filesystem.fs.PathObserver
import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.service
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.CoroutineContext

internal class VortexPathObserver(
    internal val id: Int,
    path: Path,
    types: Int,
    context: CoroutineContext,
) : PathObserver {

    private val scope = CoroutineScope(context)
    private val producer = scope.produce {
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

    override val events: Flow<PathObservableKey.Event>
        get() = producer.receiveAsFlow()

    override fun cancel(cause: CancellationException?) {

    }
}