package io.github.excu101.vortex.ui.component.header.icon

import android.view.View
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

class IconTextHeaderViewHolder(
    private val root: IconTextHeaderView,
) : ViewHolder<IconTextHeaderItem>(root) {

    override fun bind(item: IconTextHeaderItem) = with(root) {
        onChanged()
        icon = item.value.icon
        title = item.value.title
    }

    override fun bindSelection(isSelected: Boolean) {
        root.updateSelection(isSelected)
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