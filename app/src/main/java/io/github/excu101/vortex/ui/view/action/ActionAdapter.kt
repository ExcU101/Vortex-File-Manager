package io.github.excu101.vortex.ui.view.action

import android.view.ViewGroup
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.vortex.ui.base.BaseAdapter

class ActionAdapter : BaseAdapter<GroupAction, ActionViewHolder>(
    differ = ActionDiffer()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        return ActionViewHolder(ActionView(parent.context))
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}