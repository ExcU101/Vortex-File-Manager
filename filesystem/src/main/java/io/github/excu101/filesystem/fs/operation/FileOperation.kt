package io.github.excu101.filesystem.fs.operation

import io.github.excu101.filesystem.fs.path.Path

abstract class FileOperation {

    private val _observers: MutableList<FileOperationObserver> = mutableListOf()
    private val observes: List<FileOperationObserver>
        get() = _observers

    abstract val id: Int
    abstract suspend fun perform()

    internal fun subscribe(observer: List<FileOperationObserver>) {
        _observers.addAll(observer)
    }

    protected fun notify(value: Path) {
        observes.forEach { observer ->
            observer.onAction(value = value)
        }
    }

    protected fun notify(error: Throwable) {
        observes.forEach { observer ->
            observer.onError(error)
        }
    }

    protected fun notify(unit: Unit = Unit) {
        observes.forEach { observer ->
            observer.onComplete()
        }
    }
}

