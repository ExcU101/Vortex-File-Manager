package io.github.excu101.vortex.utils

import android.os.Parcel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.parcelize.Parceler

object PathParceler : Parceler<Path> {

    override fun create(parcel: Parcel): Path {
        return FileProvider.parsePath(input = parcel.readString() ?: "")
    }

    override fun Path.write(parcel: Parcel, flags: Int) {
        parcel.writeString(toString())
    }

}