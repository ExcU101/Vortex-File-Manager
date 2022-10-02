package io.github.excu101.vortex.provider

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.utils.unixRename
import io.github.excu101.vortex.service.data.ParcelableFileOperation
import io.github.excu101.vortex.service.utils.PathParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
class SwapNameOperation(
    private val first: @WriteWith<PathParceler> Path,
    private val second: @WriteWith<PathParceler> Path,
) : ParcelableFileOperation() {

    // Currently not working
    override suspend fun perform() {
        FileProvider.rerun(unixRename(first, second))
        FileProvider.rerun(unixRename(second, first))
    }


}