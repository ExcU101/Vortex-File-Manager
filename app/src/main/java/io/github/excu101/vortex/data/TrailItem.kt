package io.github.excu101.vortex.data

import android.os.Parcelable
import io.github.excu101.vortex.data.storage.StorageItem
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.adapter.Item
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
data class TrailItem(
    val isSelected: Boolean,
    val isLast: Boolean,
    override val value: StorageItem,
) : Item<StorageItem>, Parcelable {
    override val id: Long
        get() = value.id

    override val type: Int
        get() = ItemViewTypes.TRAIL
}