package io.github.excu101.vortex.navigation.page

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PageLifecycle(private val owner: LifecycleOwner) : Lifecycle() {

    private var current = State.INITIALIZED
    private val observers = mutableListOf<LifecycleObserver>()

    internal fun updateState(state: State) {
        val isBack = state < current

        if (isBack) {
            Event.downFrom(state)?.let(::notifyObservers)
        } else {
            Event.upFrom(state)?.let(::notifyObservers)
        }

        current = state
    }

    override fun addObserver(observer: LifecycleObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: LifecycleObserver) {
        observers.remove(observer)
    }

    override fun getCurrentState(): State = current

    fun notifyObservers(event: Event) {
        observers.forEach { observer ->
            if (observer is LifecycleEventObserver) {
                observer.onStateChanged(owner, event)
            }
        }
    }

}