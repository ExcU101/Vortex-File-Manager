package io.github.excu101.vortex.ui.component.trail

import android.view.ViewGroup
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.adapter.selection.SelectionListAdapterImpl

class TrailAdapter : SelectionListAdapterImpl<PathItem, TrailViewHolder>(
    differ = TrailDiffer()
) {
    init {
        setHasStableIds(true)
    }

    private var selected: Int = -1

    fun updateSelected(index: Int) {
        val old = selected
        selected = index
        notifyItemChanged(index)
        notifyItemChanged(old)
    }

    override fun getItemId(position: Int) = item(position).hashCode().toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        return TrailViewHolder(root = TrailItemView(context = parent.context))
    }

    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        val item = item(position)
        holder.bind(item = item)
        holder.isSelected = isSelected(position)
        holder.isArrowVisible = isLast(position)
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

    fun isLast(position: Int) = position != currentList.lastIndex

    override fun isSelected(position: Int) = selected == position

    override val selectedCount: Int
        get() = if (selected >= 0) 1 else 0

}