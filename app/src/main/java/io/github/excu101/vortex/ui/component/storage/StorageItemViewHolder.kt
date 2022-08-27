package io.github.excu101.vortex.ui.component.storage

import android.view.View
import androidx.annotation.DrawableRes
import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.filesystem.fs.utils.properties
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.StorageItem
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder

class StorageItemViewHolder(
    private val root: StorageItemView,
) : ViewHolder<StorageItem>(root) {

    override fun bind(item: StorageItem): Unit = with(root) {
        setTitle(item.value.name)
        setInfo(parseInfo(item.value))
        setIcon(parseIcon(item.value))
    }

    override fun unbind(): Unit = with(root) {
        setTitle(null)
        setInfo(null)
        setIcon(null)
    }

    override fun bindListener(listener: View.OnClickListener) {
        root.setOnClickListener(listener)
        root.setOnIconClickListener(listener)
    }

    override fun bindLongListener(listener: View.OnLongClickListener) {
        root.setOnLongClickListener(listener)
        root.setOnIconLongClickListener(listener)
    }

    @DrawableRes
    private fun parseIcon(value: FileUnit): Int {
        return if (value.isDirectory) R.drawable.ic_folder_24 else R.drawable.ic_file_24
    }

    private fun parseInfo(value: FileUnit): String {
        val content = if (value.isDirectory) {
            when (val count = value.path.properties().count) {
                0 -> "No item"
                1 -> "One item"
                else -> "Items $count"
            }
        } else {
            value.attrs.lastAccessTime.toString()
        }

        val size = value.size.toString()

        return "$content | $size"
    }

}