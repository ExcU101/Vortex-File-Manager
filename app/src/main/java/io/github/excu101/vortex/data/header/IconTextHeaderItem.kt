package io.github.excu101.vortex.data.header

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortex.ui.component.ItemViewTypes.ICON_TEXT_HEADER
import io.github.excu101.vortex.ui.component.header.action.IconTextHeaderView
import io.github.excu101.vortex.ui.component.header.action.IconTextHeaderViewHolder
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

data class IconTextHeaderItem(
    override val value: Action,
) : Item<Action> {

    constructor(title: String, icon: Drawable) : this(Action(title, icon))

    override val id: Long
        get() = hashCode().toLong()

    override val type: Int
        get() = ICON_TEXT_HEADER

    companion object : ViewHolderFactory<IconTextHeaderItem> {
        override fun produceView(parent: ViewGroup): View {
            return IconTextHeaderView(parent.context)
        }

        override fun produceViewHolder(child: View): ViewHolder<IconTextHeaderItem> {
            return IconTextHeaderViewHolder(child as IconTextHeaderView)
        }
    }

}