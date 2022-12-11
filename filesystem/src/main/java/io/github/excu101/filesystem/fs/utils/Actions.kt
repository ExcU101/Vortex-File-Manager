package io.github.excu101.filesystem.fs.utils

import io.github.excu101.filesystem.fs.operation.action.*
import io.github.excu101.filesystem.fs.path.Path

fun CopyAction(
    source: Path,
    destination: Path,
): CopyAction = object : CopyAction {
    override val source: Path = source
    override val destination: Path = destination
}

fun CreateDirectoryAction(
    source: Path,
): CreateDirectoryAction = object : CreateDirectoryAction {
    override val source: Path = source
}

fun CreateFileAction(
    source: Path,
): CreateFileAction = object : CreateFileAction {
    override val source: Path = source
}

fun CreateSymbolicLinkAction(
    target: Path,
    destination: Path,
): CreateSymbolicLinkAction = object : CreateSymbolicLinkAction {
    override val target: Path = target
    override val destination: Path = destination
}

fun CutAction(
    source: Path,
    destination: Path,
): MoveAction = object : MoveAction {
    override val source: Path = source
    override val destination: Path = destination
}

fun DeleteAction(
    source: Path,
): DeleteAction = object : DeleteAction {
    override val source: Path = source
}

fun RenameAction(
    source: Path,
    destination: Path,
): RenameAction = object : RenameAction {
    override val source: Path = source
    override val destination: Path = destination
}

fun ReadAction(): ReadAction = object : ReadAction {}
fun WriteAction(bytes: Long): WriteAction = object : WriteAction {
    override val bytes: Long = bytes
}