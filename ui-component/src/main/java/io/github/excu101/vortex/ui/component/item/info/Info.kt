package io.github.excu101.vortex.ui.component.item.info

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Info(
    val value: String,
    val description: String,
) : Parcelable