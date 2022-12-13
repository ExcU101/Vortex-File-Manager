package io.github.excu101.filesystem.fs.operation

abstract class FileOperation {

    // Interface marker
    interface Action

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

