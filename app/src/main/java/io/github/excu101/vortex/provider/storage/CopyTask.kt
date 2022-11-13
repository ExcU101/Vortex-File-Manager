package io.github.excu101.vortex.provider.storage

import io.github.excu101.filesystem.fs.path.Path

data class CopyTask(
    val sources: Set<Path>,
)