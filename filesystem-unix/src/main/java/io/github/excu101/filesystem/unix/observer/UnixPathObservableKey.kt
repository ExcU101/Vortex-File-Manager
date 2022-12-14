package io.github.excu101.filesystem.unix.observer

import android.content.Context
import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.observer.event.CreateEvent
import io.github.excu101.filesystem.unix.calls.UnixObserverCalls
import io.github.excu101.filesystem.unix.observer.UnixMasks.OVERFLOW
import io.github.excu101.filesystem.unix.observer.UnixMasks.maskWith
import io.github.excu101.filesystem.unix.path.UnixPath
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class UnixPathObservableKey(
    private val service: UnixPathObserverService,
    internal val path: UnixPath,
    internal val descriptor: Int,
) : PathObservableKey {

    private enum class State {
        SIGNALED, READY
    }

    private var _events = mutableListOf<PathObservableKey.Event>()

    private val mutex = Mutex()

    private var actual = true

    private var state = State.READY

    override val isActual: Boolean
        get() = actual && service.isOpen

    internal fun registerEvent(event: PathObservableKey.Event) {

//            val last = _events.last()
//            if (event == last) {
//                if (event.context == last.context) return
//            }

        _events.add(event)
        notifyService()
    }

    private fun notifyService() {
        if (state == State.READY) {
            state = State.SIGNALED
            service.enqueueKey(key = this)
        }
    }

    suspend fun setNotActual() = mutex.withLock {
        actual = false
    }

    override suspend fun pollEvents(): List<PathObservableKey.Event> = mutex.withLock {
        val events = _events
        _events = mutableListOf()
        return events
    }

    override suspend fun reset(): Boolean {
        if (state == State.SIGNALED) {
            if (_events.isEmpty()) {
                state = State.READY
            } else {
                service.enqueueKey(key = this)
            }
        }
        return isActual
    }

    override suspend fun cancel() {
        service.removeObservable(observable = this@UnixPathObservableKey)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnixPathObservableKey

        if (service != other.service) return false
        if (path != other.path) return false
        if (descriptor != other.descriptor) return false
        if (_events != other._events) return false
        if (actual != other.actual) return false
        if (state != other.state) return false

        return true
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + descriptor
        return result
    }
}