package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.DeleteAction
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.observer.UnixMasks.maskWith

internal class UnixDeleteOperation(
    private val data: Collection<Path>,
    options: Int = Options.Empty,
) : FileOperation() {

    private val notifySubItems = options maskWith Options.Delete.NotifyAll

    private inline fun onOption(prediction: Boolean, action: () -> Unit) {
        if (prediction) action()
    }

    override suspend fun perform() {
        data.forEach { path ->
            val action = DeleteAction(path)
            try {
                action(action)
                if (FileProvider.readAttrs<BasicAttrs>(path).isDirectory) {
                    onOption(notifySubItems) { action(action) }
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
                onOption(notifySubItems) { action(DeleteAction(child)) }
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