package io.github.excu101.vortex.ui.component.header.action

import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder

class ActionHeaderViewHolder(
    private val root: ActionHeaderView,
) : ViewHolder<ActionHeaderItem>(root) {

    override fun bind(item: ActionHeaderItem) {
        root.icon = item.value.icon
        root.title = item.value.title
    }

    override fun unbind() {
        root.title = null
    }

}