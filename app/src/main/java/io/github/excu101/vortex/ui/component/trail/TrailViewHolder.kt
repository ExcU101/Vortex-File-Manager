package io.github.excu101.vortex.ui.component.trail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.data.TrailItem
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder

class TrailViewHolder(private val root: TrailItemView) : ViewHolder<TrailItem>(root) {

    override fun bind(item: TrailItem) {
        root.setTitle(value = item.value.path.getName().toString())
        root.setArrowVisibility(!item.isLast)
        root.updateSelection(item.isSelected)
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