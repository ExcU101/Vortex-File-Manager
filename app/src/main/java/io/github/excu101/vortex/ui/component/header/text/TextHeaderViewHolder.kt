package io.github.excu101.vortex.ui.component.header.text

import io.github.excu101.vortex.data.header.TextHeaderItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

class TextHeaderViewHolder(private val root: TextHeaderView) : ViewHolder<TextHeaderItem>(root) {

    override fun bind(item: TextHeaderItem): Unit = with(root) {
        onChanged()
        setTitle(item.value)
    }

    override fun unbind(): Unit = with(root) {
        setTitle(null)
    }

}