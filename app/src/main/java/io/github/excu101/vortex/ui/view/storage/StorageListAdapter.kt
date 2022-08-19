package io.github.excu101.vortex.ui.view.storage

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.vortex.ui.base.BaseAdapter

class StorageListAdapter : BaseAdapter<FileUnit, StorageItemViewHolder>(
    differ = StorageUnitDiffer()
) {
    init {
        setHasStableIds(true)
    }

    private var selected: List<FileUnit> = listOf()

    fun replaceSelectedFiles(list: List<FileUnit>) {
        val differ = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = selected.size

            override fun getNewListSize(): Int = list.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[newItemPosition].path == selected[oldItemPosition].path
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[newItemPosition].path == selected[oldItemPosition].path
            }
        }
        val result = DiffUtil.calculateDiff(differ)
        selected = list
        result.dispatchUpdatesTo(this)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).path.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageItemViewHolder {
        return StorageItemViewHolder(
            root = StorageItemView(context = parent.context)
        )
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