package io.github.excu101.vortex.service.data

import android.os.Parcelable
import io.github.excu101.filesystem.fs.operation.FileOperation
import kotlinx.parcelize.Parcelize

@Parcelize
open class ParcelableOption : FileOperation.Option, Parcelable