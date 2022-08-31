package io.github.excu101.vortex.ui.component.storage

import android.text.format.Formatter
import android.view.ViewGroup
import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.data.header.TextHeaderItem
import io.github.excu101.vortex.data.storage.StorageItem
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.adapter.Item
import io.github.excu101.vortex.ui.component.adapter.diff.ListItemDiffer
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.adapter.selection.SelectionListAdapterImpl
import io.github.excu101.vortex.ui.component.header.action.ActionHeaderView
import io.github.excu101.vortex.ui.component.header.action.ActionHeaderViewHolder
import io.github.excu101.vortex.ui.component.header.text.TextHeaderView
import io.github.excu101.vortex.ui.component.header.text.TextHeaderViewHolder
import io.github.excu101.vortex.ui.component.storage.standard.StorageItemView
import io.github.excu101.vortex.ui.component.storage.standard.StorageItemViewHolder
import java.util.*

class StorageItemAdapter : SelectionListAdapterImpl<Item<*>, ViewHolder<*>>(
    differ = ListItemDiffer
) {
    init {
        setHasStableIds(true)
    }

    private val selected = mutableListOf<StorageItem>()

    override val selectedCount: Int
        get() = selected.count()

    val storageItemCount: Int
        get() = currentList.count { it is StorageItem }

    override fun getItemId(position: Int): Long = item(position).id

    override fun isSelected(position: Int) = isSelected(item(position))

    fun isSelected(item: Item<*>): Boolean {
        if (item !is StorageItem) return false

        return item in selected
    }

    override fun select(item: Item<*>) {
        val isSelected = isSelected(item)
        if (isSelected) selected.remove(item as StorageItem)
        else selected.add(item as StorageItem)

        notifyItemChanged(position(item))
    }

    override fun select(items: Collection<Item<*>>) {
        val changed = mutableListOf<StorageItem>()
        val iterator = items.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item is StorageItem) {
                if (item !in selected) {
                    changed.add(item)
                }
            }
        }

        for (item in changed) {
            notifyItemChanged(position(item))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> {
        return when (viewType) {
            ItemViewTypes.TEXT_HEADER -> {
                TextHeaderViewHolder(TextHeaderView(parent.context))
            }

            ItemViewTypes.ACTION_HEADER -> {
                ActionHeaderViewHolder(ActionHeaderView(parent.context))
            }

            else -> {
                StorageItemViewHolder(StorageItemView(parent.context))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<*>, position: Int) {
        val item = item(position)

        when (val viewType = getItemViewType(position)) {
            ItemViewTypes.TEXT_HEADER -> {
                with(holder as TextHeaderViewHolder) {
                    bind(item as TextHeaderItem)
                }
            }

            ItemViewTypes.ACTION_HEADER -> {
                with(holder as ActionHeaderViewHolder) {
                    bind(item as ActionHeaderItem)
                }
            }

            else -> {
                with(holder as StorageItemViewHolder) {
                    bind(item as StorageItem)
                    bindSelection(isSelected(item))

                    bindListener { view ->
                        notify(
                            view = view,
                            item = item,
                            position = position
                        )
                    }

                    bindLongListener { view ->
                        notifyLong(
                            view = view,
                            item = item,
                            position = position
                        )
                    }

                    bindSelectionListener { view ->
                        select(item)
                        notifySelection(
                            view = view,
                            item = item,
                            position = position,
                            isSelected = isSelected(item)
                        )
                    }

                    bindSelectionLongListener { view ->
                        select(item)
                        notifyLongSelection(
                            view = view,
                            item = item,
                            position = position,
                            isSelected = !isSelected(item)
                        )
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