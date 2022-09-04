package io.github.excu101.vortex.ui.component.storage.simple

import androidx.annotation.DrawableRes
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder

class SimpleStorageItemViewHolder(
    private val root: SimpleStorageItemView,
) : ViewHolder<PathItem>(root) {

    override fun bind(item: PathItem) = with(root) {
        setTitle(item.name)
        setIcon(parseIcon(item))
    }

    override fun unbind() = with(root) {
        clearItems()
    }

    @DrawableRes
    private fun parseIcon(item: PathItem): Int {
        return if (item.isDirectory) R.drawable.ic_folder_24 else R.drawable.ic_file_24
    }

}