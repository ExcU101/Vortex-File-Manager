package io.github.excu101.vortex.provider.command

import io.github.excu101.filesystem.fs.path.Path

data class CutFilesCommand(
    val sources: Set<Path>,
    val dest: Path,
    val options: Int
) : Command