package io.github.excu101.vortex.provider.command

import io.github.excu101.filesystem.fs.path.Path

data class CreateFileCommand(
    val dest: Path,
    val mode: Int,
    val flags: Int
) : Command