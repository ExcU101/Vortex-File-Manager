package io.github.excu101.filesystem.fs.utils

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import java.io.File

inline fun <reified T : Path> parsePath(input: String): T {
    return FileProvider.parsePath(input = input) as T
}

fun String.toPath() = FileProvider.parsePath(input = this)

inline fun <reified T : Path> String.toSpecificPath(): T = parsePath(input = this)

fun Iterable<String>.toPaths() = map { it.toPath() }

fun Collection<String>.toPath() {
    FileProvider.parsePath(first = "", other = toTypedArray())
}

fun File.asPath() = FileProvider.parsePath(input = path)

fun Path.asFile() = File(this.toString())