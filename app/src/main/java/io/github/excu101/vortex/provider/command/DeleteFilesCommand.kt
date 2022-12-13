package io.github.excu101.vortex.provider.command

import io.github.excu101.filesystem.fs.path.Path

data class DeleteFilesCommand(val sources: Set<Path>) : Command