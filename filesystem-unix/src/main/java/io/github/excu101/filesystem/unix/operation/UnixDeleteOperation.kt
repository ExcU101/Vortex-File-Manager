package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.UnixCalls

internal class UnixDeleteOperation(
    private val data: Collection<Path>,
) : FileOperation() {

    override val id: Int = IdRegister.register(IdRegister.Type.OPERATION)

    override suspend fun perform() {
        data.forEach { path ->
            try {
                notify(path)
                if (FileProvider.readAttrs<BasicAttrs>(path).isDirectory) {
                    deleteRecursively(path)
                } else {
                    UnixCalls.unlink(path = path.bytes)
                }
            } catch (error: Exception) {
                notify(error)
                return
            }
        }
        notify()
    }

    private fun deleteRecursively(path: Path) {
        path.system.provider.newDirectorySteam(path).use { stream ->
            stream.forEach { child ->
                if (FileProvider.readAttrs<BasicAttrs>(child).isDirectory) {
                    deleteRecursively(child)
                } else {
                    UnixCalls.unlink(path = path.bytes)
                }
            }
        }

        UnixCalls.removeDirectory(path.bytes)
    }

}