package io.github.excu101.filesystem.unix.utils

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.operation.*

fun unixDelete(
    data: Collection<Path>,
): FileOperation = UnixDeleteOperation(
    data = data
)

fun unixCopy(
    sources: Set<Path>,
    dest: Path,
): FileOperation = UnixCopyOperation(
    sources = sources,
    dest = dest
)

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
    flags: Set<FileOperation.Option> = setOf(
        Options.Open.CreateNew,
        Options.Open.Read,
        Options.Open.Write,
        Options.Open.Append
    ),
    mode: Int = 777,
): FileOperation = UnixCreateFileOperation(
    source,
    flags,
    mode
)