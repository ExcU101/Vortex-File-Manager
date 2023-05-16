package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.path.UnixPath

internal class UnixChangeOwnerOperation(
    private val path: Path,
    private val owner: Int,
    private val group: Int
) : FileOperation() {

    override suspend fun perform() {
        UnixCalls.changeOwner(
            path.bytes,
            owner,
            group
        )

        completion()
    }

}