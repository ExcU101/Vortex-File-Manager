package io.github.excu101.filesystem

import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.FileStore
import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.error.NotAnyProviderInstalled
import io.github.excu101.filesystem.fs.observer.PathObserverService
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.provider.FileSystemProvider
import kotlinx.coroutines.Job
import kotlin.reflect.KClass

object FileProvider {

    private val systems = arrayListOf<FileSystem>()

    val systemCount: Int
        get() = systems.size

    private var defaultSystem: FileSystem? = null

    val stores: Iterable<FileStore>
        get() = getSystem().stores

    private fun getSystem(): FileSystem {
        return defaultSystem ?: throw NotAnyProviderInstalled()
    }

    private fun getProvider(): FileSystemProvider {
        return getSystem().provider
    }

    private fun getSystem(scheme: String?): FileSystem {
        return systems.find { it.scheme == scheme } ?: getSystem()
    }

    private fun getProvider(scheme: String?): FileSystemProvider {
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

    fun getFileStores(scheme: String? = null): Iterable<FileStore> {
        if (scheme == null) return stores
        return getSystem(scheme).stores
    }

    fun getFileStore(path: Path): FileStore {
        return try {
            getProvider(path).getFileStore(path)
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

    fun uninstall(system: FileSystem) {
        systems.remove(system)
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

    fun parsePath(input: String, scheme: String? = null): Path {
        return getSystem(scheme).getPath(first = input)
    }

    fun parsePath(first: String, vararg other: String): Path {
        return getSystem().getPath(first = first, other = other)
    }

    fun newPathObserverService(scheme: String? = null): PathObserverService {
        return getSystem(scheme).newPathObserverService()
    }

}