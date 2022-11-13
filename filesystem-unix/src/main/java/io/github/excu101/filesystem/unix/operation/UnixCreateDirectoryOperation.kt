package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.CreateDirectoryAction
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.error.UnixException

internal class UnixCreateDirectoryOperation(
    private val path: Path,
    private val mode: Int,
) : FileOperation() {

    override suspend fun perform() {
        action(CreateDirectoryAction(path))
        try {
            UnixCalls.mkdir(path.bytes, mode)
        } catch (exception: UnixException) {
            error(exception)
            return
        }

        completion()
    }
}