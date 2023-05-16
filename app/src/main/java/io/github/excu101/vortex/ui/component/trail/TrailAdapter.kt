package io.github.excu101.vortex.ui.component.trail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.utils.TrailItem

class TrailAdapter : ItemAdapter<PathItem>(
    ItemViewTypes.TrailItem to Factory
) {

    private var selected: Int = -1

    fun updateSelected(index: Int) {
        val old = selected
        selected = index
        notifyItemChanged(index)
        notifyItemChanged(old)
    }

    override fun replace(items: List<PathItem>) {
        super.replace(items, null)
    }

    override fun getItemViewType(position: Int): Int = ItemViewTypes.TrailItem

    override fun onBindViewHolder(holder: ViewHolder<PathItem>, position: Int) {
        holder as TrailViewHolder
        super.onBindViewHolder(holder, position)
        holder.isArrowVisible = position != list.lastIndex
    }

    override fun isSelected(position: Int) = selected == position

    override fun isSelected(item: PathItem): Boolean {
        return isSelected(position(item))
    }

    private object Factory : ViewHolderFactory<PathItem> {
        override fun produceView(parent: ViewGroup): View {
            return TrailItemView(parent.context)
        }

        override fun produceViewHolder(child: View): ViewHolder<PathItem> {
            return TrailViewHolder(child as TrailItemView)
        }
    }

}