package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.DeleteAction
import io.github.excu101.filesystem.unix.UnixCalls

internal class UnixDeleteOperation(
    private val data: Collection<Path>,
) : FileOperation() {

    override suspend fun perform() {
        data.forEach { path ->
            val action = DeleteAction(path)
            try {
                action(action)
                if (FileProvider.readAttrs<BasicAttrs>(path).isDirectory) {
                    deleteRecursively(path)
                } else {
                    UnixCalls.unlink(path = path.bytes)
                }
            } catch (error: Exception) {
                error(error)
                return
            }
        }
        completion()
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