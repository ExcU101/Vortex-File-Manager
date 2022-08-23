package io.github.excu101.vortex.ui.view.storage

import android.view.View
import androidx.annotation.DrawableRes
import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.filesystem.fs.utils.properties
import io.github.excu101.vortex.R
import io.github.excu101.vortex.ui.base.BaseViewHolder

class StorageItemViewHolder(val root: StorageItemView) : BaseViewHolder<FileUnit>(root) {

    override fun bind(value: FileUnit) = with(root) {
        setTitle(value.name)
        setInfo(parseInfo(value))
        setIcon(parseIcon(value))
    }

    fun bindSelection(isSelected: Boolean) {
        root.updateSelection(isSelected)
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        root.setOnClickListener(listener)
        root.setOnIconClickListener(listener)
    }

    fun setOnLongClickListener(listener: View.OnLongClickListener) {
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