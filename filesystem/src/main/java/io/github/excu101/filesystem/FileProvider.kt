package io.github.excu101.filesystem

import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.error.FileSystemErrorHandler
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.coroutines.Job
import kotlin.reflect.KClass

object FileProvider {

    private val systems = arrayListOf<FileSystem>()
    private val handlers = arrayListOf<FileSystemErrorHandler>()

    val systemCount: Int
        get() = systems.size

    fun newDirStream(path: Path): DirectoryStream<Path> {
        try {
            return systems.last().provider.newDirectorySteam(path)
        } catch (error: Throwable) {
            notify(error = error)
            return DirectoryStream
        }
    }

    fun isDirectory(path: Path): Boolean {
        return systems.last().provider.readAttrs(path, BasicAttrs::class).isDirectory
    }

    fun <T : BasicAttrs> readAttrs(path: Path, type: KClass<T>): T {
        return systems.last().provider.readAttrs(path, type)
    }

    inline fun <reified T : BasicAttrs> readAttrs(path: Path): T = readAttrs(path, T::class)

    fun install(system: FileSystem) {
        if (!systems.contains(system)) {
            systems.add(system)
        }
    }

    fun install(handler: FileSystemErrorHandler) {
        handlers.add(handler)
    }

    fun uninstall(system: FileSystem) {
        systems.remove(system)
    }

    fun uninstall(handler: FileSystemErrorHandler) {
        handlers.remove(handler)
    }

    internal fun notify(error: Throwable) {
        handlers.forEach { handler -> handler.onFileSystemError(error = error) }
    }

    fun runOperation(operation: FileOperation, observers: List<FileOperationObserver> = listOf()): Job {
        return systems.last().provider.runOperation(operation = operation, observers = observers)
    }

    fun parsePath(input: String): Path {
        return systems.last().getPath(first = input)
    }

    fun parsePath(first: String, vararg other: String): Path {
        return systems.last().getPath(first = first, other = other)
    }

}