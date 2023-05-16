package io.github.excu101.filesystem.unix.utils

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.operation.*
import io.github.excu101.filesystem.unix.operation.UnixCopyOperation
import io.github.excu101.filesystem.unix.operation.UnixCreateDirectoryOperation
import io.github.excu101.filesystem.unix.operation.UnixCreateFileOperation
import io.github.excu101.filesystem.unix.operation.UnixCreateLinkOperation
import io.github.excu101.filesystem.unix.operation.UnixCutOperation
import io.github.excu101.filesystem.unix.operation.UnixDeleteOperation
import io.github.excu101.filesystem.unix.operation.UnixRenameOperation

fun unixDelete(
    data: Collection<Path>,
): FileOperation = UnixDeleteOperation(
    data = data
)

fun unixChangeOwner(
    source: Path,
    owner: Int,
    group: Int
): FileOperation = UnixChangeOwnerOperation(
    path = source,
    owner = owner,
    group = group,
)

fun unixCopy(
    sources: Set<Path>,
    dest: Path,
    options: Int,
): FileOperation = UnixCopyOperation(
    sources = sources,
    dest = dest,
    options = options
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
    flags: Int,
    mode: Int = 777,
): FileOperation = UnixCreateFileOperation(
    source,
    flags,
    mode
)

fun unixCut(
    sources: Set<Path>,
    dest: Path,
    options: Int,
): FileOperation = UnixCutOperation(
    sources,
    dest,
    options
)