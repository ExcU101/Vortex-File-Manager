package io.github.excu101.filesystem.fs.utils

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias Action = (action: FileOperation.Action) -> Unit
typealias Error = (value: Throwable) -> Unit
typealias Completion = () -> Unit

typealias SuspendAction = suspend (action: FileOperation.Action) -> Unit
typealias SuspendError = suspend (value: Throwable) -> Unit
typealias SuspendCompletion = suspend () -> Unit

inline fun FileOperationObserver(
    crossinline action: Action,
    crossinline error: Error,
    crossinline completion: Completion,
): FileOperationObserver {
    return object : FileOperationObserver {
        override fun onAction(action: FileOperation.Action) {
            action(action)
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
        override fun onAction(action: FileOperation.Action) {
            scope.launch { action(action) }
        }

        override fun onError(value: Throwable) {
            scope.launch { error(value) }
        }

        override fun onComplete() {
            scope.launch { completion() }
        }
    }
}