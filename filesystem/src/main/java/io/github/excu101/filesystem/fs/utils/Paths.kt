package io.github.excu101.filesystem.fs.utils

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.DirectoryStream.Filter
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.channel.AsyncFileChannel
import io.github.excu101.filesystem.fs.observer.PathObservableEventType
import io.github.excu101.filesystem.fs.observer.PathObserverService
import io.github.excu101.filesystem.fs.operation.option.Options.Open.Append
import io.github.excu101.filesystem.fs.operation.option.Options.Open.CreateNew
import io.github.excu101.filesystem.fs.operation.option.Options.Open.Read
import io.github.excu101.filesystem.fs.operation.option.Options.Open.Write
import io.github.excu101.filesystem.fs.path.Path
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

const val defaultMode = 777

infix fun Path.resolve(other: String) = resolve(system.getPath(other))

fun Path.resolve(bytes: ByteArray, charset: Charset = UTF_8): Path = resolve(String(bytes, charset))

fun Path.startsWith(prefix: String) = startsWith(system.getPath(first = prefix))

suspend fun Path.service(
    vararg types: PathObservableEventType,
): PathObserverService {
    val service = FileProvider.newPathObserverService(scheme)
    service.register(this, *types)
    return service
}

@Suppress(names = ["UNCHECKED_CAST"])
fun <P : Path> DirectoryStream<P>.toList(): List<P> {
    return use { stream ->
        val list = mutableListOf<P>()
        stream.forEach { path ->
            list.add(path as P)
        }
        return@use list
    }
}

inline fun <reified T : BasicAttrs> Path.attrs(): T {
    return system.provider.readAttrs(this, T::class)
}

inline val Path.store
    get() = system.provider.getFileStore(path = this)

inline val Path.list
    get() = system.provider.newDirectorySteam(this).use { it.toList() }

inline val Path.flow
    get() = system.provider.newDirectoryFlow(this)

fun Path.flow(
    filter: Filter<Path> = Filter.acceptAll()
) = system.provider.newDirectoryFlow(this, filter)

inline val Path.count: Int
    get() = system.helper?.getCount(this) ?: -1

inline val Path.stream
    get() = system.provider.newDirectorySteam(path = this)

fun Path.channel(
    flags: Int = CreateNew and Read and Write and Append,
    mode: Int = defaultMode,
): AsyncFileChannel {
    return system.provider.newReactiveFileChannel(
        path = this,
        flags = flags,
        mode = mode
    )
}

inline val Path.directoryCount: Int
    get() = system.helper?.getDirectoryCount(this) ?: -1

inline val Path.fileCount: Int
    get() = system.helper?.getFileCount(this) ?: -1

inline val Path.directorySize: Size
    get() = Size(system.helper?.getDirectorySize(this) ?: 0L)