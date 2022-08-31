package io.github.excu101.vortex.ui.component.storage.standard

import android.os.StatFs
import android.view.View
import androidx.annotation.DrawableRes
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.utils.properties
import io.github.excu101.pluginsystem.ui.theme.ReplacerThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.storage.StorageItem
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.theme.key.fileListItemCountKey
import io.github.excu101.vortex.ui.component.theme.key.fileListItemEmptyKey
import io.github.excu101.vortex.ui.component.theme.key.fileListItemsCountKey
import io.github.excu101.vortex.ui.component.theme.key.specialSymbol

class StorageItemViewHolder(
    private val root: StorageItemView,
) : ViewHolder<StorageItem>(root) {

    override fun bind(item: StorageItem): Unit = with(root) {
        setTitle(item.name)
        setInfo(parseInfo(item))
        setIcon(parseIcon(item))
    }

    fun bindSelection(isSelected: Boolean) = with(root) {
        updateSelection(isSelected = isSelected)
    }

    override fun unbind(): Unit = with(root) {
        setTitle(null)
        setInfo(null)
        setIcon(null)
    }

    override fun bindListener(listener: View.OnClickListener): Unit = with(root) {
        setOnClickListener(listener)
    }

    override fun bindLongListener(listener: View.OnLongClickListener): Unit = with(root) {
        setOnIconLongClickListener(listener)
    }

    override fun bindSelectionListener(listener: View.OnClickListener): Unit = with(root) {
        setOnIconClickListener(listener)
    }

    override fun bindSelectionLongListener(listener: View.OnLongClickListener) = with(root) {
        setOnLongClickListener(listener)
    }

    override fun unbindListeners(): Unit = with(root) {
        setOnClickListener(null)
        setOnIconClickListener(null)

        setOnLongClickListener(null)
        setOnIconLongClickListener(null)
    }

    @DrawableRes
    private fun parseIcon(value: StorageItem): Int {
        return if (value.isDirectory) R.drawable.ic_folder_24 else R.drawable.ic_file_24
    }

    private fun parseInfo(item: StorageItem): String {
        val content = if (item.isDirectory) {
            when (val count = item.value.properties().count) {
                0 -> ThemeText(fileListItemEmptyKey)
                1 -> ThemeText(fileListItemCountKey)
                else -> ReplacerThemeText(
                    key = fileListItemsCountKey,
                    old = specialSymbol,
                    new = "$count"
                )
            }
        } else {
            item.attrs.lastAccessTime.toString()
        }
        val size = if (item.isDirectory) {
            val stat = StatFs(item.value.toString())
            Size(bytes = stat.totalBytes - stat.availableBytes)
        } else {
            item.size
        }

        return "$content | $size"
    }

}