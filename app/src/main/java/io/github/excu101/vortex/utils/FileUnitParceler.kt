package io.github.excu101.vortex.utils

import android.os.Parcel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.FileUnit
import kotlinx.parcelize.Parceler

object FileUnitParceler : Parceler<FileUnit> {
    override fun create(parcel: Parcel): FileUnit {
        return FileUnit(FileProvider.parsePath(input = parcel.readString() ?: ""))
    }

    override fun FileUnit.write(parcel: Parcel, flags: Int) {
        parcel.writeString(path.toString())
    }
}