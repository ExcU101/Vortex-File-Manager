package io.github.excu101.vortex.ui.component.trail

import android.view.View
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

class TrailViewHolder(private val root: TrailItemView) : ViewHolder<PathItem>(root) {

    override fun bind(item: PathItem): Unit = with(root) {
        setTitle(value = item.name)
    }

    var isArrowVisible: Boolean = false
        set(value) {
            root.setArrowVisibility(value)
        }

    override fun unbind() {
        root.setTitle(null)
        root.setArrowVisibility(false)
    }

    override fun bindListener(listener: View.OnClickListener) {
        root.setOnClickListener(listener)
    }

    override fun bindSelection(isSelected: Boolean) {
        root.updateSelection(isSelected)
    }

    override fun bindLongListener(listener: View.OnLongClickListener) {
        root.setOnLongClickListener(listener)
    }

    override fun unbindListeners() {
        root.setOnClickListener(null)
        root.setOnLongClickListener(null)
    }

}