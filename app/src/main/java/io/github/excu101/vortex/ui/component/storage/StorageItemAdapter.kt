package io.github.excu101.vortex.ui.component.storage

import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.Selection
import io.github.excu101.vortex.data.header.TextHeaderItem
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.adapter.Item
import io.github.excu101.vortex.ui.component.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.header.text.TextHeaderView
import io.github.excu101.vortex.ui.component.header.text.TextHeaderViewHolder

class StorageItemAdapter : ItemAdapter<Item<*>>(
    ItemViewTypes.TEXT_HEADER to (TextHeaderItem as ViewHolderFactory<Item<*>>),
    ItemViewTypes.STORAGE to (PathItem as ViewHolderFactory<Item<*>>)
) {
    init {
        setHasStableIds(true)
    }

    private val selection = Selection<Long>()

    val headerItemCount: Int
        get() = list.count { it is TextHeaderItem }

    val storageItemCount: Int
        get() = list.count { it is PathItem }

    val selectedCount: Int
        get() = selection.size

    override fun getItemId(position: Int): Long = item(position).id

    fun choose(key: PathItem) {
        if (key.id in selection) deselect(key)
        else select(key)
    }

    fun select(key: PathItem) {
        if (selection.add(key = key.id)) {
            notifyItemChanged(position(key))
        }
    }

    fun deselect(key: PathItem) {
        if (selection.remove(key = key.id)) {
            notifyItemChanged(position(key))
        }
    }

    fun replace(
        items: List<Item<*>>,
    ) = replace(
        items,
        StorageItemDiffer(list, items)
    )

}