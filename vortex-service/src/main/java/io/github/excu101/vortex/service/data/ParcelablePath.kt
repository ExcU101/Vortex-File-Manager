package io.github.excu101.vortex.service.data

import android.os.Parcelable
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.service.utils.PathParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
class ParcelablePath(
    val src: @WriteWith<PathParceler> Path,
) : Parcelable

fun Path.asParcelable() = ParcelablePath(src = this)