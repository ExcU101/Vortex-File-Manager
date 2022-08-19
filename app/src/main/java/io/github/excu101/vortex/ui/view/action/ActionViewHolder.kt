package io.github.excu101.vortex.ui.view.action

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.vortex.ui.base.BaseViewHolder

class ActionViewHolder(private val root: ActionView) : BaseViewHolder<GroupAction>(root) {
    override fun bind(value: GroupAction) {
        root.setIcon(value.icon)
        root.setTitle(value.name)
    }
}