package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.error.UnixException

class UnixCreateDirectoryOperation(
    private val path: Path,
    private val mode: Int,
) : FileOperation() {

    override val id: Int
        get() = IdRegister.register(IdRegister.Type.OPERATION)

    override suspend fun perform() {
        try {
            notify(path)
            UnixCalls.mkdir(path.bytes, mode)
        } catch (exception: UnixException) {
            notify(exception)
            return
        }

        notify()
    }
}