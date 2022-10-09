package io.github.excu101.vortex.ui.component.storage

import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.Selection
import io.github.excu101.vortex.data.storage.MutablePathItemMapSet
import io.github.excu101.vortex.data.storage.PathItemMapSet
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.header.icon.IconTextHeaderItem
import io.github.excu101.vortex.ui.component.header.text.TextHeaderItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.utils.STORAGE

class StorageItemAdapter : ItemAdapter<Item<*>>(
    ItemViewTypes.TEXT_HEADER to (TextHeaderItem as ViewHolderFactory<Item<*>>),
    ItemViewTypes.ICON_TEXT_HEADER to (IconTextHeaderItem as ViewHolderFactory<Item<*>>),
    ItemViewTypes.STORAGE to (PathItem as ViewHolderFactory<Item<*>>)
) {
    init {
        setHasStableIds(true)
    }

    private val selected = Selection<PathItem>()

    fun replace(files: PathItemMapSet) {
        val changedFiles = MutablePathItemMapSet()
        val iterator = selected.iterator()
        while (iterator.hasNext()) {
            val file = iterator.next()
            if (file !in files) {
                iterator.remove()
                changedFiles.add(file)
            }
        }
        for (file in files) {
            if (file !in selected) {
                selected.add(file)
                changedFiles.add(file)
            }
        }
        for (file in changedFiles) {
            val position = position(file)
            notifyItemChanged(position)
        }
    }

    override fun isSelected(item: Item<*>): Boolean {
        if (item !is PathItem) return super.isSelected(item)
        return selected.contains(item)
    }

}