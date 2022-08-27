package io.github.excu101.vortex.data

import android.os.Parcelable
import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.pluginsystem.model.DataHolder
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.adapter.Item
import io.github.excu101.vortex.utils.FileUnitParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
data class TrailItem(
    val isSelected: Boolean,
    val isLast: Boolean,
    override val value: @WriteWith<FileUnitParceler> FileUnit,
) : Item<FileUnit>, Parcelable {
    override val id: Long
        get() = value.path.hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.TRAIL
}