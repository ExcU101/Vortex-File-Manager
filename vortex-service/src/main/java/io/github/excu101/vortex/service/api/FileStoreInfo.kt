package io.github.excu101.vortex.service.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileStoreInfo(
    val totalSpace: Long,
    val usableSpace: Long,
    val unallocatedSpace: Long,
) : Parcelable