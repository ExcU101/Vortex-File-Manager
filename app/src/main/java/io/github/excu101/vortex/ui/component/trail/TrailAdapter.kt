package io.github.excu101.vortex.ui.component.trail

import android.view.ViewGroup
import io.github.excu101.vortex.data.TrailItem
import io.github.excu101.vortex.ui.component.adapter.diff.ItemDiffer
import io.github.excu101.vortex.ui.component.adapter.diff.ListItemDiffer
import io.github.excu101.vortex.ui.component.adapter.selection.SelectionAdapterImpl
import io.github.excu101.vortex.ui.component.adapter.selection.SelectionListAdapterImpl

class TrailAdapter : SelectionListAdapterImpl<TrailItem, TrailViewHolder>(
    differ = TrailDiffer
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        return TrailViewHolder(root = TrailItemView(context = parent.context))
    }

    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        val item = item(position)
        holder.bind(item)
        holder.bindListener { view ->
            notify(view = view, value = item, position = position)
        }
        holder.bindLongListener { view ->
            notifyLong(view = view, value = item, position = position)
        }
    }

    override fun onViewRecycled(holder: TrailViewHolder) {
        holder.unbind()
        holder.unbindListeners()
    }

    override fun isSelected(position: Int): Boolean = false

}