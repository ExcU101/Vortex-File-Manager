package io.github.excu101.vortex.ui.view.action

import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.pluginsystem.model.GroupAction

class ActionDiffer : DiffUtil.ItemCallback<GroupAction>() {
    override fun areItemsTheSame(oldItem: GroupAction, newItem: GroupAction): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: GroupAction, newItem: GroupAction): Boolean {
        return oldItem == newItem
    }
}