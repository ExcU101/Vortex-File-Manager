package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.CreateSymbolicLinkAction
import io.github.excu101.filesystem.unix.UnixCalls

internal class UnixCreateLinkOperation(
    private val target: Path,
    private val link: Path,
) : FileOperation() {

    override suspend fun perform() {
        action(CreateSymbolicLinkAction(target, link))
        try {
            UnixCalls.symlink(target = target.bytes, link = link.bytes)
        } catch (exception: Exception) {
            error(exception)
            return
        }

        completion()
    }

}