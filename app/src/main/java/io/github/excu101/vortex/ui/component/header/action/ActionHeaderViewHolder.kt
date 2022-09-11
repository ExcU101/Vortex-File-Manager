package io.github.excu101.vortex.ui.component.header.action

import android.view.View
import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

class ActionHeaderViewHolder(
    private val root: ActionHeaderView,
) : ViewHolder<ActionHeaderItem>(root) {

    override fun bind(item: ActionHeaderItem) {
        root.icon = item.value.icon
        root.title = item.value.title
    }

    override fun bindListener(listener: View.OnClickListener) = with(root) {
        setOnClickListener(listener)
    }

    override fun unbindListeners() = with(root) {
        setOnClickListener(null)
    }

    override fun unbind() {
        root.title = null
    }

}