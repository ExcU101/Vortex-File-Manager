package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.CreateDirectoryAction
import io.github.excu101.filesystem.fs.utils.CreateFileAction

internal class UnixCreateFileOperation(
    private val path: Path,
    private val flags: Set<Option>,
    private val mode: Int,
) : FileOperation() {

    override suspend fun perform() {
        action(CreateFileAction(path))
        try {
            path.system.provider.newReactiveFileChannel(path, flags, mode).close()
        } catch (exception: Exception) {
            error(error = exception)
            return
        }
        completion()
    }

}