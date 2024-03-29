package io.github.excu101.vortex.ui.component.storage

import android.graphics.drawable.Drawable
import io.github.excu101.vortex.theme.FormatterThemeText
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemNameKey

interface StorageCell {

    var title: String?

    var info: String?

    var icon: Drawable?

    var isCellSelected: Boolean

    var isBookmarked: Boolean

}

interface RecyclableStorageCell : StorageCell, ViewHolder.RecyclableView<PathItem> {

    override fun onBind(item: PathItem) {
        title = io.github.excu101.vortex.theme.FormatterThemeText(
            key = io.github.excu101.vortex.theme.key.text.storage.item.fileListItemNameKey,
            item.name
        )
        info = item.info
    }

    override fun onUnbind() {
        title = null
        icon = null
        info = null
    }

}