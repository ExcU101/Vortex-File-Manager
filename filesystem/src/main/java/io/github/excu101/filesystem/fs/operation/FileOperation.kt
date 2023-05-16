package io.github.excu101.filesystem.fs.operation

import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

abstract class FileOperation {

    companion object {
        private val _id = AtomicInteger()
    }

    // Interface marker
    interface Action

    val id: Int = _id.incrementAndGet()

    private val _observers: MutableList<FileOperationObserver> = mutableListOf()
    protected val observes: List<FileOperationObserver>
        get() = _observers

    suspend operator fun invoke() {
        return perform()
    }

    abstract suspend fun perform()

    internal fun subscribe(observer: List<FileOperationObserver>) {
        _observers.addAll(observer)
    }

    protected fun action(action: Action) {
        observes.forEach { observer ->
            observer.onAction(action = action)
        }
    }

    protected fun error(error: Throwable) {
        observes.forEach { observer ->
            observer.onError(error)
        }
    }

    protected fun completion() {
        observes.forEach(FileOperationObserver::onComplete)
    }

}

