package io.github.excu101.vortex.provider.storage

import android.os.Parcelable
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.service.utils.PathParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
data class Storage(
    val name: String,
    val path: @WriteWith<PathParceler> Path,
) : Parcelable