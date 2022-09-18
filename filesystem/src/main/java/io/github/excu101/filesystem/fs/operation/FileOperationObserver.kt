package io.github.excu101.filesystem.fs.operation

import io.github.excu101.filesystem.fs.path.Path

interface FileOperationObserver {

    fun onAction(value: Path)

    fun onError(value: Throwable)

    fun onComplete()

}

inline fun observer(
    crossinline onNext: (value: Path) -> Unit = {},
    crossinline onError: (value: Throwable) -> Unit = {},
    crossinline onComplete: () -> Unit = {},
): FileOperationObserver {
    return object : FileOperationObserver {
        override fun onAction(value: Path) {
            onNext(value)
        }

        override fun onError(value: Throwable) {
            onError(value)
        }

        override fun onComplete() {
            onComplete()
        }
    }
}