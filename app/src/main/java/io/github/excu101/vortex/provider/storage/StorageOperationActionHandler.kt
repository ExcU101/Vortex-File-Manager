package io.github.excu101.vortex.provider.storage

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.action.*
import io.github.excu101.vortex.provider.FileOperationActionHandler
import javax.inject.Inject

class StorageOperationActionHandler @Inject constructor() : FileOperationActionHandler<String?> {

    override fun resolveMessage(action: FileOperation.Action): String? {
        return when (action) {

            is DeleteAction -> "Deleting ${action.source.getName()}"

            is CopyAction -> "Coping ${action.source.getName()} to ${action.destination.getName()}"

            is MoveAction -> "Moving file ${action.source.getName()} to ${action.destination.getName()}"

            is CreateFileAction -> "Creating file ${action.source.getName()}"

            is CreateDirectoryAction -> "Creating directory $${action.source.getName()}"

            is WriteAction -> "Wrote bytes: ${action.bytes}"

            is CopyPathAction -> "Copying path ${action.source.getName()}"

            else -> null
        }
    }

}