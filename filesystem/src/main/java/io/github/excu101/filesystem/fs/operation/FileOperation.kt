package io.github.excu101.filesystem.fs.operation

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path

abstract class FileOperation {

    private val _observers: MutableList<FileOperationObserver> = mutableListOf()
    protected val observes: List<FileOperationObserver>
        get() = _observers

    protected fun FileProvider.rerun(operation: FileOperation) {
        runOperation(operation, observes)
    }

    abstract val id: Int

    internal suspend fun call() {
        perform()
    }

    suspend operator fun invoke() {
        return perform()
    }

    protected abstract suspend fun perform()

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

