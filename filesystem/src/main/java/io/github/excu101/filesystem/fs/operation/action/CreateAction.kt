package io.github.excu101.filesystem.fs.operation.action

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path

interface CreateAction : FileOperation.Action {
    val source: Path
}

interface CreateDirectoryAction : CreateAction
interface CreateFileAction : CreateAction