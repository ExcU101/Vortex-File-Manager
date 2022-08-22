package io.github.excu101.vortex.ui.view.storage

import android.view.ViewGroup
import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.vortex.ui.base.BaseAdapter

class StorageListAdapter : BaseAdapter<FileUnit, StorageItemViewHolder>(
    differ = StorageUnitDiffer()
) {
    init {
        setHasStableIds(true)
    }

    private val selected = mutableListOf<FileUnit>()

    override fun getItemId(position: Int): Long {
        return getItem(position).path.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageItemViewHolder {
        return StorageItemViewHolder(
            root = StorageItemView(context = parent.context)
        )
    }

    fun addSelected(selected: Collection<FileUnit>) {
        this.selected.addAll(selected)
        for (s in selected) {
            if (s in currentList) {
                notifyItemChanged(indexOf(s))
            }
        }
    }

    fun removeSelected(selected: Collection<FileUnit>) {
        this.selected.removeAll(selected)
        for (s in selected) {
            if (s in currentList) {
                notifyItemChanged(indexOf(s))
            }
        }
    }

    override fun onBindViewHolder(holder: StorageItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(value = item)
        holder.bindSelection(isSelected = selected.contains(item))
        holder.setOnClickListener { view ->
            notify(view = view, value = item, position = position)
        }
        holder.setOnLongClickListener { view ->
            notifyLong(view = view, value = item, position = position)
        }
    }

}