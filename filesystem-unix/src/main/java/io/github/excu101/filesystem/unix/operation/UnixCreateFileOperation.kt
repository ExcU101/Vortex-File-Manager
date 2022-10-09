package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.fs.attr.Option
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path

internal class UnixCreateFileOperation(
    private val path: Path,
    private val flags: Set<Option>,
    private val mode: Int,
) : FileOperation() {

    override val id: Int = IdRegister.register(IdRegister.Type.OPERATION)

    override suspend fun perform() {
        try {
            path.system.provider.newReactiveFileChannel(path, flags, mode).close()
        } catch (exception: Exception) {
            notify(error = exception)
            return
        }
        notify()
    }

}