package io.github.excu101.vortex.ui.component.trail

import android.view.View
import io.github.excu101.vortex.data.TrailItem
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder

class TrailViewHolder(private val root: TrailItemView) : ViewHolder<TrailItem>(root) {

    override fun bind(item: TrailItem): Unit = with(root) {
        setTitle(value = item.value.name)
        setArrowVisibility(!item.isLast)
    }

    fun updateSelection(isSelected: Boolean): Unit = with(root) {
        updateSelection(isSelected)
    }

    override fun unbind() {
        root.setTitle(null)
    }

    override fun bindListener(listener: View.OnClickListener) {
        root.setOnClickListener(listener)
    }

    override fun bindLongListener(listener: View.OnLongClickListener) {
        root.setOnLongClickListener(listener)
    }

    override fun unbindListeners() {
        root.setOnClickListener(null)
        root.setOnLongClickListener(null)
    }

}