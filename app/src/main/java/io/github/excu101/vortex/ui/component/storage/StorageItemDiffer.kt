package io.github.excu101.vortex.ui.component.storage

import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.vortex.ui.component.list.adapter.Item

class StorageItemDiffer(
    private val old: List<Item<*>>,
    private val new: List<Item<*>>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].id == new[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}