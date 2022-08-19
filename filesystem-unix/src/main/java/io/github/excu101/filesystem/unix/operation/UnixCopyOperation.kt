package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path

class UnixCopyOperation(
    private val sources: Set<Path>,
    private val dest: Path,
) : FileOperation() {

    override val id: Int = IdRegister.register(IdRegister.Type.OPERATION)

    override suspend fun perform() {
        sources.forEach { source ->
            notify(source)
        }
        notify()
    }
}