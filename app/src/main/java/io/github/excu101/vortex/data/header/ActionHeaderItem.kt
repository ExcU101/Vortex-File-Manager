package io.github.excu101.vortex.data.header

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortex.ui.component.ItemViewTypes.ACTION_HEADER
import io.github.excu101.vortex.ui.component.adapter.Item

data class ActionHeaderItem(
    override val value: Action,
) : Item<Action> {

    override val id: Long
        get() = hashCode().toLong()

    override val type: Int
        get() = ACTION_HEADER

}