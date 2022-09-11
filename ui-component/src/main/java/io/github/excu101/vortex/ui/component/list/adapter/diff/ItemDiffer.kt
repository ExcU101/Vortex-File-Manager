package io.github.excu101.vortex.ui.component.list.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.vortex.ui.component.list.adapter.Item

class ItemDiffer(
    private val old: List<Item<*>>,
    private val new: List<Item<*>>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean = old[oldItemPosition].id == new[newItemPosition].id

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean = old[oldItemPosition] == new[newItemPosition]

}