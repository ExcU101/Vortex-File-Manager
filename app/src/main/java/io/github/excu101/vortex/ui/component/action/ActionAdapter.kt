package io.github.excu101.vortex.ui.component.action

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.ui.component.adapter.diff.ListItemDiffer
import io.github.excu101.vortex.ui.component.adapter.selection.SelectionListAdapterImpl
import io.github.excu101.vortex.ui.component.header.action.ActionHeaderView
import io.github.excu101.vortex.ui.component.header.action.ActionHeaderViewHolder

class ActionAdapter : SelectionListAdapterImpl<ActionHeaderItem, ActionHeaderViewHolder>(
    differ = object : DiffUtil.ItemCallback<ActionHeaderItem>() {
        override fun areItemsTheSame(
            oldItem: ActionHeaderItem,
            newItem: ActionHeaderItem,
        ): Boolean = ListItemDiffer.areItemsTheSame(oldItem, newItem)


        override fun areContentsTheSame(
            oldItem: ActionHeaderItem,
            newItem: ActionHeaderItem,
        ): Boolean = ListItemDiffer.areContentsTheSame(oldItem, newItem)
    }
) {
    override val selectedCount: Int
        get() = 0

    override fun isSelected(position: Int): Boolean {
        return false
    }

    override fun select(item: ActionHeaderItem) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionHeaderViewHolder {
        return ActionHeaderViewHolder(root = ActionHeaderView(parent.context))
    }

    override fun onBindViewHolder(holder: ActionHeaderViewHolder, position: Int) {
        holder.bind(item(position))
    }


}