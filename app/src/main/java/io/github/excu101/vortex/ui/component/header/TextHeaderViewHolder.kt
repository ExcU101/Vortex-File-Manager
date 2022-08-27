package io.github.excu101.vortex.ui.component.header

import io.github.excu101.vortex.data.TextHeaderItem
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder

class TextHeaderViewHolder(private val root: TextHeaderView) : ViewHolder<TextHeaderItem>(root) {

    override fun bind(item: TextHeaderItem): Unit = with(root) {
        setTitle(item.value)
    }

    override fun unbind(): Unit = with(root) {
        setTitle(null)
    }

}