package io.github.excu101.vortex.service.utils

import android.util.Log
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.utils.*
import io.github.excu101.vortex.service.data.ParcelableFileOperation
import io.github.excu101.vortex.service.data.ParcelablePath

fun parcelableUnixDelete(path: Collection<ParcelablePath>) {
    object : ParcelableFileOperation() {
        override suspend fun perform() {
            unixDelete(path.map { it.src }).perform()
        }
    }
}

fun parcelableUnixRename(src: ParcelablePath, dest: ParcelablePath): ParcelableFileOperation {
    return object : ParcelableFileOperation() {
        override suspend fun perform() {
            unixRename(src.src, dest.src).perform()
        }
    }
}

fun parcelableUnixCreateDirectory(
    source: ParcelablePath,
    mode: Int = 777,
): ParcelableFileOperation {
    object : ParcelableFileOperation() {
        override suspend fun perform() = unixCreateDirectory(source.src, mode)()
    }
    return object : ParcelableFileOperation() {
        override suspend fun perform() {
            Log.v("Loggable", "Called")
        }
    }
}

fun parcelableUnixCreateFile(
    source: Path,
    flags: Set<FileOperation.Option> = setOf(
        Options.Open.Write,
        Options.Open.Read,
        Options.Open.CreateNew,
        Options.Open.Append
    ),
    mode: Int = 777,
): ParcelableFileOperation {
    return object : ParcelableFileOperation() {
        override suspend fun perform() {
            unixCreateFile(source, flags, mode).perform()
        }
    }
}

fun parcelableUnixCreateSymbolicLink(
    target: Path,
    link: Path,
): ParcelableFileOperation {
    return object : ParcelableFileOperation() {
        override suspend fun perform() {
            unixCreateSymbolicLink(target, link).perform()
        }
    }
}
