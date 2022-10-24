package io.github.excu101.filesystem.unix.utils

import io.github.excu101.filesystem.fs.attr.Option
import io.github.excu101.filesystem.fs.attr.StandardOptions
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.operation.*
import kotlin.reflect.KClass

fun unixDelete(
    data: Collection<Path>,
): FileOperation = UnixDeleteOperation(
    data
)

fun unixDeleteClass(): KClass<out FileOperation> = UnixDeleteOperation::class

fun unixCreateSymbolicLink(
    target: Path,
    link: Path,
): FileOperation = UnixCreateLinkOperation(
    target = target,
    link = link,
)

fun unixRename(
    source: Path,
    dest: Path,
): FileOperation = UnixRenameOperation(
    source = source,
    dest = dest
)

fun unixCreateDirectory(
    source: Path,
    mode: Int = 777,
): FileOperation = UnixCreateDirectoryOperation(
    source,
    mode
)

fun unixCreateFile(
    source: Path,
    flags: Set<Option> = setOf(
        StandardOptions.CREATE_NEW,
        StandardOptions.READ,
        StandardOptions.WRITE,
        StandardOptions.APPEND
    ),
    mode: Int = 777,
): FileOperation = UnixCreateFileOperation(
    source,
    flags,
    mode
)