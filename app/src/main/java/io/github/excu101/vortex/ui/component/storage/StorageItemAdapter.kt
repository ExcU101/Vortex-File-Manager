package io.github.excu101.vortex.ui.component.storage

import android.view.ViewGroup
import android.widget.TextView
import io.github.excu101.vortex.data.TextHeaderItem
import io.github.excu101.vortex.data.StorageItem
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.adapter.Item
import io.github.excu101.vortex.ui.component.adapter.diff.ListItemDiffer
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.adapter.selection.SelectionListAdapterImpl
import io.github.excu101.vortex.ui.component.header.TextHeaderView
import io.github.excu101.vortex.ui.component.header.TextHeaderViewHolder

class StorageItemAdapter : SelectionListAdapterImpl<Item<*>, ViewHolder<*>>(
    differ = ListItemDiffer
) {

    override fun isSelected(position: Int): Boolean {
        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> {
        return when (viewType) {
            ItemViewTypes.TEXT_HEADER -> {
                TextHeaderViewHolder(TextHeaderView(parent.context))
            }

            else -> {
                StorageItemViewHolder(StorageItemView(parent.context))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<*>, position: Int) {
        val item = item(position)
        val viewType = getItemViewType(position)

        when (viewType) {
            ItemViewTypes.TEXT_HEADER -> {
                with(holder as TextHeaderViewHolder) {
                    bind(item as TextHeaderItem)
                }
            }

            else -> {
                with(holder as StorageItemViewHolder) {
                    bind(item as StorageItem)
                    bindListener { view ->
                        notify(view = view, value = item, position = position)
                    }
                    bindLongListener { view ->
                        notifyLong(view = view, value = item, position = position)
                    }
                }
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder<*>) {
        holder.unbind()
        holder.unbindListeners()
    }

    override fun getItemViewType(position: Int): Int {
        return item(position).type
    }

}