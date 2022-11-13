package io.github.excu101.vortex.provider

import io.github.excu101.filesystem.fs.operation.FileOperation

interface FileOperationActionHandler {

    fun resolveMessage(action: FileOperation.Action): String?

}