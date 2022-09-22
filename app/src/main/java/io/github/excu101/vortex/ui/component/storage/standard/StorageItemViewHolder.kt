package io.github.excu101.vortex.ui.component.storage.standard

import android.view.View
import io.github.excu101.filesystem.fs.utils.count
import io.github.excu101.pluginsystem.ui.theme.ReplacerThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.utils.convertToThemeText
import io.github.excu101.vortex.utils.parseThemeType

class StorageItemViewHolder(
    private val root: StorageItemView,
) : ViewHolder<PathItem>(root) {

    override fun bind(item: PathItem): Unit = with(root) {
        onChanged()
        setTitle(ReplacerThemeText(
            key = fileListItemNameKey,
            old = specialSymbol,
            new = item.name
        ))
        parseIconRes(item) {
            setIcon(it)
        }
        parseInfo(item) {
            setInfo(it)
        }
    }

    override fun bindSelection(isSelected: Boolean): Unit = with(root) {
        updateSelection(isSelected = isSelected)
    }

    override fun unbind(): Unit = with(root) {
        setTitle(null)
        setInfo(null)
        setIcon(null)
    }

    override fun bindListener(listener: View.OnClickListener): Unit = with(root) {
        setOnIconClickListener(listener)
        setOnClickListener(listener)
    }

    override fun bindLongListener(listener: View.OnLongClickListener): Unit = with(root) {
        setOnIconLongClickListener(listener)
        setOnLongClickListener(listener)
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

    private inline fun parseIconRes(
        item: PathItem,
        onResult: (id: Int) -> Unit,
    ) {
        val icon = if (item.isDirectory) R.drawable.ic_folder_24 else R.drawable.ic_file_24
        onResult(icon)
    }

    private inline fun parseInfo(
        item: PathItem,
        onResult: (String) -> Unit,
    ) {
        val content = if (item.isDirectory) {
            when (val count = item.value.count) {
                -1 -> ThemeText(fileListItemEmptyKey)
                0 -> ThemeText(fileListItemEmptyKey)
                1 -> ThemeText(fileListItemCountKey)
                else -> ReplacerThemeText(
                    key = fileListItemsCountKey,
                    old = specialSymbol,
                    new = count.toString()
                )
            }
        } else {
            item.lastModifiedTime.toString()
        }

        val size = if (item.isFile) ReplacerThemeText(
            key = fileListItemSizeKey,
            old = specialSymbol,
            new = item.size.convertToThemeText()
        ) else ""

        val mimeType = if (item.isFile) item.mimeType.parseThemeType() else ""

        var result = ""

        if (content.isNotEmpty()) {
            result += content
        }

        if (size.isNotEmpty()) {
            result += " | $size"
        }

        if (mimeType.isNotEmpty()) {
            result += " | $mimeType"
        }

        onResult.invoke(result)
    }

}