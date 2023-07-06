package io.github.excu101.vortex.service.storage

import io.github.excu101.filesystem.fs.PathObserver
import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.operation.PathOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.service.notification.VortexNotificationManager
import kotlinx.coroutines.newSingleThreadContext
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

class VortexStorageComponent(
    private val notifier: VortexNotificationManager,
) {
    private val ids = AtomicInteger(0)
    private val observers = mutableMapOf<Path, PathObserver>()

    fun hasObserver(path: Path) = observers[path] != null

    fun delete(
        path: Path,
        observer: PathOperationObserver? = null,
    ) {

    }

    suspend fun observe(
        path: Path,
        types: Int,
        recursive: Boolean = false,
        context: CoroutineContext = newSingleThreadContext("$path observer thread"),
        onEvent: (PathObservableKey.Event) -> Unit,
    ) {
        val observer = VortexPathObserver(ids.getAndIncrement(), path, types, context)

        observer.events.collect { event ->
            onEvent(event)
            notifier.update(id = observer.id, event = event.type, path = path)
        }
    }

    fun closeObserver(path: Path): VortexStorageComponent {
        if (path.isEmpty) return this

        observers.remove(path)?.cancel()
        return this
    }

}