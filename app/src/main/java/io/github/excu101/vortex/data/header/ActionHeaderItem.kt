package io.github.excu101.vortex.data.header

import android.view.View
import android.view.ViewGroup
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortex.ui.component.ItemViewTypes.ACTION_HEADER
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.header.action.ActionHeaderView
import io.github.excu101.vortex.ui.component.header.action.ActionHeaderViewHolder

data class ActionHeaderItem(
    override val value: Action,
) : Item<Action> {

    override val id: Long
        get() = hashCode().toLong()

    override val type: Int
        get() = ACTION_HEADER

    companion object : ViewHolderFactory<ActionHeaderItem> {
        override fun produceView(parent: ViewGroup): View {
            return ActionHeaderView(parent.context)
        }

        override fun produceViewHolder(child: View): ViewHolder<ActionHeaderItem> {
            return ActionHeaderViewHolder(child as ActionHeaderView)
        }
    }

}