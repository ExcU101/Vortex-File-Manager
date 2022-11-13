package io.github.excu101.vortex.service.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VortexServiceInfo(
    val processId: Int,
    val installedFileSystemCount: Int,
    val connectedCount: Int,
) : Parcelable