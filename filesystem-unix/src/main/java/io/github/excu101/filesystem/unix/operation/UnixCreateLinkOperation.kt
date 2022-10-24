package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.UnixCalls

internal class UnixCreateLinkOperation(
    private val target: Path,
    private val link: Path,
) : FileOperation() {

    override val id: Int = IdRegister.register(IdRegister.Type.OPERATION)

    override suspend fun perform() {
        notify(target)
        try {
            UnixCalls.symlink(target = target.bytes, link = link.bytes)
        } catch (exception: Exception) {
            notify(exception)
            return
        }

        notify()
    }

}