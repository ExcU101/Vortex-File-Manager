package io.github.excu101.vortex.data.storage

import android.os.Parcelable
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.service.utils.PathParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
data class PathItemTag(
    val name: String,
    val paths: List<@WriteWith<PathParceler> Path>,
) : Parcelable