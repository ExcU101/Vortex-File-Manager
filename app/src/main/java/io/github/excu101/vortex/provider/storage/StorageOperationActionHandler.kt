package io.github.excu101.vortex.provider.storage

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.action.CopyAction
import io.github.excu101.filesystem.fs.operation.action.CreateDirectoryAction
import io.github.excu101.filesystem.fs.operation.action.CreateFileAction
import io.github.excu101.filesystem.fs.operation.action.DeleteAction
import io.github.excu101.vortex.provider.FileOperationActionHandler

class StorageOperationActionHandler : FileOperationActionHandler {

    override fun resolveMessage(action: FileOperation.Action): String? {
        return when (action) {
            is DeleteAction -> {
                "Deleting ${action.source.getName()}"
            }
            is CopyAction -> {
                "Coping ${action.source.getName()} to ${action.destination.getName()}"
            }
            is CreateFileAction -> {
                "Creating file ${action.source.getName()}"
            }
            is CreateDirectoryAction -> {
                "Creating directory $${action.source.getName()}"
            }
            else -> null
        }
    }

}