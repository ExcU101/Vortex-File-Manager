package io.github.excu101.vortex.ui.component.list.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.vortex.ui.component.list.adapter.Item

object ListItemDiffer : DiffUtil.ItemCallback<Item<*>>() {

    override fun areItemsTheSame(
        oldItem: Item<*>,
        newItem: Item<*>,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Item<*>,
        newItem: Item<*>,
    ) = oldItem == newItem

}