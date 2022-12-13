package io.github.excu101.vortex.provider

import io.github.excu101.filesystem.fs.operation.FileOperation

interface FileOperationActionHandler<T> {

    fun resolveMessage(action: FileOperation.Action): T

}