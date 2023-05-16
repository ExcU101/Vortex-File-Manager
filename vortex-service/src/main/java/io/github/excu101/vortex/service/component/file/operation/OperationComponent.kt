package io.github.excu101.vortex.service.component.file.operation

import io.github.excu101.filesystem.fs.operation.FileOperation
import kotlinx.coroutines.flow.StateFlow

interface OperationComponent {

    val state: StateFlow<OperationComponentState>

    fun add(
        operation: FileOperation
    ): OperationComponent

    fun run()

}