package io.github.excu101.vortex.service.utils

import android.os.Parcel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.parcelize.Parceler

object PathParceler : Parceler<Path> {

    override fun create(parcel: Parcel): Path {
        val scheme = parcel.readString()
        val path = parcel.readString()
        return FileProvider.parsePath(scheme = scheme, input = path ?: "")
    }

    override fun Path.write(parcel: Parcel, flags: Int) {
        parcel.writeString(scheme)
        parcel.writeString(toString())
    }

}