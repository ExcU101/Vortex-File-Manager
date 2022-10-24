package io.github.excu101.vortex.ui.component.item.icon

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.action
import io.github.excu101.vortex.ui.component.ItemViewTypes.iconTextItem
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory

data class IconTextHeaderItem(
    override val value: Action,
) : Item<Action> {

    constructor(title: String, icon: Drawable) : this(action(title, icon))

    override val id: Long = hashCode().toLong()

    override fun equals(other: Any?): Boolean = value == other

    override fun hashCode(): Int = value.hashCode()

    override val type: Int
        get() = iconTextItem

    companion object : ViewHolderFactory<IconTextHeaderItem> {
        override fun produceView(parent: ViewGroup): View {
            return IconTextHeaderView(parent.context)
        }
    }
}

fun ItemScope<Item<*>>.iconText(value: Action) {
    add(IconTextHeaderItem(value))
}