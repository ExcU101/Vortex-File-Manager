package io.github.excu101.vortex.service.data

import android.os.Parcelable
import io.github.excu101.filesystem.fs.attr.Option
import io.github.excu101.filesystem.fs.attr.StandardOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class ParcelableOption(
    private val option: StandardOptions,
) : Option, Parcelable