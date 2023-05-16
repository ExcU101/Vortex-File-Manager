package io.github.excu101.vortex.service.component.file.operation

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.observer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OperationComponentImpl : OperationComponent {

    private val _state: MutableStateFlow<OperationComponentState> = MutableStateFlow(
        OperationComponentState.Idle
    )
    override val state: StateFlow<OperationComponentState>
        get() = _state.asStateFlow()

    private val _stack = mutableMapOf<Int, FileOperation?>()

    override fun add(
        operation: FileOperation
    ): OperationComponent {
        _stack[operation.id] = operation
        return this
    }

    override fun run() {
        _stack.values.forEach { operation ->
            operation?.let {
                var time = 0L
                FileProvider.runOperation(
                    operation = it,
                    observer = observer(
                        onAction = { action ->
                            time += System.currentTimeMillis()
                            gettActionOperation(
                                operation = operation,
                                action = action
                            )
                        },
                        onError = { error ->
                            getErrorOperation(
                                operation = operation,
                                error = error
                            )
                        },
                        onComplete = {
                            time -= System.currentTimeMillis()
                            completeOperation(operation, time)
                        }
                    )
                )
            }
        }
    }

    private fun gettActionOperation(operation: FileOperation, action: FileOperation.Action) {
        MainScope().launch {
            _state.emit(OperationComponentState.OperationAction(operation, action))
        }
    }

    private fun getErrorOperation(operation: FileOperation, error: Throwable) {
        MainScope().launch {
            _stack[operation.id] = null
            _state.emit(OperationComponentState.OperationError(operation, error))
        }
    }

    private fun completeOperation(
        operation: FileOperation,
        millis: Long
    ) {
        MainScope().launch {
            _stack[operation.id] = null
            _state.emit(OperationComponentState.OperationCompleted(operation, millis))
        }
    }

}