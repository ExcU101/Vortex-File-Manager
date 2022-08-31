package io.github.excu101.vortex.ui.component.storage.simple

import androidx.annotation.DrawableRes
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.storage.StorageItem
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder

class SimpleStorageItemViewHolder(
    private val root: SimpleStorageItemView,
) : ViewHolder<StorageItem>(root) {

    override fun bind(item: StorageItem) = with(root) {
        setTitle(item.name)
        setIcon(parseIcon(item))
    }

    override fun unbind() = with(root) {
        clearItems()
    }

    @DrawableRes
    private fun parseIcon(item: StorageItem): Int {
        return if (item.isDirectory) R.drawable.ic_folder_24 else R.drawable.ic_file_24
    }

}