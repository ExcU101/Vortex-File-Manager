package io.github.excu101.filesystem.fs.operation.action

import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path

interface CopyAction : FileOperation.Action {
    val source: Path
    val destination: Path
}