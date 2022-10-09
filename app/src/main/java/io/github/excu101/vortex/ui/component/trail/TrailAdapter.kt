package io.github.excu101.vortex.ui.component.trail

import android.view.View
import android.view.ViewGroup
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.utils.TRAIL

class TrailAdapter : ItemAdapter<PathItem>(
    ItemViewTypes.TRAIL to Factory
) {

    private var selected: Int = -1

    fun updateSelected(index: Int) {
        val old = selected
        selected = index
        notifyItemChanged(index)
        notifyItemChanged(old)
    }

    override fun getItemViewType(position: Int): Int = ItemViewTypes.TRAIL

    override fun onBindViewHolder(holder: ViewHolder<PathItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder as TrailViewHolder
        holder.isArrowVisible = position != itemCount - 1
    }

    override fun isSelected(position: Int) = selected == position

    override fun isSelected(item: PathItem): Boolean {
        return isSelected(position(item))
    }

    val selectedCount: Int
        get() = if (selected >= 0) 1 else 0

    private object Factory : ViewHolderFactory<PathItem> {
        override fun produceView(parent: ViewGroup): View {
            return TrailItemView(parent.context)
        }

        override fun produceViewHolder(child: View): ViewHolder<PathItem> {
            return TrailViewHolder(child as TrailItemView)
        }
    }

}