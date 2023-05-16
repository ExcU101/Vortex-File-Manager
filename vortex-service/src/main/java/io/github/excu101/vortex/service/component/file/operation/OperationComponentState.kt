package io.github.excu101.vortex.service.component.file.operation

import io.github.excu101.filesystem.fs.operation.FileOperation

sealed class OperationComponentState {
    object Idle : OperationComponentState()

    class OperationAction(
        val operation: FileOperation,
        val action: FileOperation.Action
    ) : OperationComponentState()

    class OperationError(
        val operation: FileOperation,
        val error: Throwable
    ) : OperationComponentState()

    class OperationCompleted(
        val operation: FileOperation,
        val millis: Long
    ) : OperationComponentState()
}