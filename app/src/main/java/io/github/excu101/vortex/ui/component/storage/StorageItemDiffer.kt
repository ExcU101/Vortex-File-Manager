package io.github.excu101.vortex.ui.component.storage

import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.vortex.data.storage.StorageItem

object StorageItemDiffer : DiffUtil.ItemCallback<StorageItem>() {
    override fun areItemsTheSame(oldItem: StorageItem, newItem: StorageItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StorageItem, newItem: StorageItem): Boolean {
        return oldItem == newItem
    }
}