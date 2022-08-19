package io.github.excu101.vortex.data

import android.os.Parcel
import android.os.Parcelable
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
class PathWrapper(private val original: Path) : Path by original, Parcelable {
    companion object : Parceler<Path> {
        override fun create(parcel: Parcel): Path {
            return FileProvider.parsePath(input = parcel.readString() ?: "")
        }

        override fun Path.write(parcel: Parcel, flags: Int) {
            parcel.writeString(toString())
        }
    }
}