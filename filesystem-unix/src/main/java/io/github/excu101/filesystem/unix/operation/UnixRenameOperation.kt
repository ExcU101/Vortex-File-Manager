package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.RenameAction
import io.github.excu101.filesystem.unix.UnixCalls

internal class UnixRenameOperation(
    private val source: Path,
    private val dest: Path,
) : FileOperation() {

    override suspend fun perform() {
        action(RenameAction(source, dest))
        try {
            UnixCalls.rename(source.bytes, dest.bytes)
        } catch (exception: Throwable) {
            error(exception)
            return
        }
        completion()
    }

}