package io.github.excu101.filesystem.fs.operation

interface PathOperationObserver {

    fun onAction(action: FileOperation.Action)

    fun onError(value: Throwable)

    fun onComplete()

}

inline fun observer(
    crossinline onAction: (action: FileOperation.Action) -> Unit = {},
    crossinline onError: (value: Throwable) -> Unit = {},
    crossinline onComplete: () -> Unit = {},
): PathOperationObserver {
    return object : PathOperationObserver {
        override fun onAction(action: FileOperation.Action) {
            onAction.invoke(action)
        }

        override fun onError(value: Throwable) {
            onError(value)
        }

        override fun onComplete() {
            onComplete()
        }
    }
}