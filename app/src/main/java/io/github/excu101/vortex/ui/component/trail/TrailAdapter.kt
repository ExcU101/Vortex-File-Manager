package io.github.excu101.vortex.ui.component.trail

import android.view.ViewGroup
import io.github.excu101.vortex.data.TrailItem
import io.github.excu101.vortex.ui.component.adapter.selection.SelectionListAdapterImpl

class TrailAdapter : SelectionListAdapterImpl<TrailItem, TrailViewHolder>(
    differ = TrailDiffer
) {
    override val selectedCount: Int
        get() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        return TrailViewHolder(root = TrailItemView(context = parent.context))
    }

    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        val item = item(position)
        holder.bind(item)
        holder.updateSelection(isSelected(item))
        holder.bindListener { view ->
            notify(view = view, item = item, position = position)
        }
        holder.bindLongListener { view ->
            notifyLong(view = view, item = item, position = position)
        }
    }

    override fun onViewRecycled(holder: TrailViewHolder) {
        holder.unbind()
        holder.unbindListeners()
    }

    override fun isSelected(position: Int) = isSelected(item(position))

    fun isSelected(item: TrailItem) = item.isSelected

    override fun select(item: TrailItem) {

    }

}