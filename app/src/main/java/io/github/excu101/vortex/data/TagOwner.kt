package io.github.excu101.vortex.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
    val color: Int,
    val title: String,
) : Parcelable

interface TagOwner {
    val tags: List<Tag>
}