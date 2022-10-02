package io.github.excu101.filesystem.fs.utils

import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias Event = (value: Path) -> Unit
typealias Error = (value: Throwable) -> Unit
typealias Completion = () -> Unit

typealias SuspendAction = suspend (value: Path) -> Unit
typealias SuspendError = suspend (value: Throwable) -> Unit
typealias SuspendCompletion = suspend () -> Unit

inline fun FileOperationObserver(
    crossinline action: Event,
    crossinline error: Error,
    crossinline completion: Completion,
): FileOperationObserver {
    return object : FileOperationObserver {
        override fun onAction(value: Path) {
            action(value)
        }

        override fun onError(value: Throwable) {
            error(value)
        }

        override fun onComplete() {
            completion()
        }
    }
}

inline fun FileOperationObserver(
    scope: CoroutineScope,
    crossinline action: SuspendAction,
    crossinline error: SuspendError,
    crossinline completion: SuspendCompletion,
): FileOperationObserver {
    return object : FileOperationObserver {
        override fun onAction(value: Path) {
            scope.launch { action(value) }
        }

        override fun onError(value: Throwable) {
            scope.launch { error(value) }
        }

        override fun onComplete() {
            scope.launch { completion() }
        }
    }
}