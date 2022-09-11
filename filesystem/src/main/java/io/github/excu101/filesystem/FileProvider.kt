package io.github.excu101.filesystem

import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.filesystem.fs.FileSystemProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.error.FileSystemErrorHandler
import io.github.excu101.filesystem.fs.error.NotAnyProviderInstalled
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.scheme
import kotlinx.coroutines.Job
import kotlin.reflect.KClass

object FileProvider {

    private val systems = arrayListOf<FileSystem>()
    private val handlers = arrayListOf<FileSystemErrorHandler>()

    val systemCount: Int
        get() = systems.size

    private var defaultSystem: FileSystem? = null

    private fun getSystem(): FileSystem {
        return defaultSystem ?: throw NotAnyProviderInstalled()
    }

    private fun getProvider(): FileSystemProvider {
        return getSystem().provider
    }

    private fun getSystem(scheme: String): FileSystem {
        for (system in systems) {
            if (system.scheme == scheme) return system
        }

        return getSystem()
    }

    private fun getProvider(scheme: String): FileSystemProvider {
        try {
            return getSystem(scheme).provider
        } catch (exception: Throwable) {
            throw exception
        }
    }

    private fun getSystem(path: Path): FileSystem {
        return getSystem(path.scheme)
    }

    private fun getProvider(path: Path): FileSystemProvider {
        return getProvider(path.scheme)
    }

    fun newDirStream(path: Path): DirectoryStream<Path> {
        return try {
            getProvider(path).newDirectorySteam(path)
        } catch (error: Throwable) {
            throw error
        }
    }

    fun <T : BasicAttrs> readAttrs(path: Path, type: KClass<T>): T {
        return getProvider(path).readAttrs(path, type)
    }

    inline fun <reified T : BasicAttrs> readAttrs(path: Path): T = readAttrs(path, T::class)

    fun install(system: FileSystem) {
        if (!systems.contains(system)) {
            systems.add(system)
        }
    }

    fun installDefault(system: FileSystem) {
        defaultSystem = system
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

    fun runOperation(operation: FileOperation, observer: FileOperationObserver): Job {
        return runOperation(operation, listOf(observer))
    }

    fun runOperation(
        operation: FileOperation,
        observers: List<FileOperationObserver> = listOf(),
    ): Job {
        return getProvider().runOperation(operation = operation, observers = observers)
    }

    fun parsePath(input: String): Path {
        return getSystem().getPath(first = input)
    }

    fun parsePath(first: String, vararg other: String): Path {
        return getSystem().getPath(first = first, other = other)
    }

}