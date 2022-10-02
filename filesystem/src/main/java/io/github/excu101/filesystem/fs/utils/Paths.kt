package io.github.excu101.filesystem.fs.utils

import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.attr.DirectoryProperties
import io.github.excu101.filesystem.fs.attr.DirectoryPropertiesImpl
import io.github.excu101.filesystem.fs.attr.Option
import io.github.excu101.filesystem.fs.attr.StandardOptions
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.path.Path

const val defaultMode = 777

infix fun Path.resolve(other: String) = resolve(system.getPath(other))

fun Path.startsWith(prefix: String) = startsWith(system.getPath(first = prefix))

fun Path.properties(): DirectoryProperties = DirectoryPropertiesImpl(directory = this)

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

inline val Path.store
    get() = system.provider.getFileStore(path = this)

inline val Path.list
    get() = system.provider.newDirectorySteam(this).toList()

inline val Path.scheme
    get() = system.scheme

inline val Path.count: Int
    get() = system.helper?.getCount(this) ?: -1

fun Path.channel(
    flags: Set<Option> = StandardOptions.values().toSet(),
    mode: Int = defaultMode,
) {
    system.provider.newReactiveFileChannel(
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
    get() = Size(system.helper?.getDirectorySize(this) ?: -1L)