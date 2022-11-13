package io.github.excu101.vortex.ui.component.item.drawer

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.action
import io.github.excu101.pluginsystem.utils.ActionScope
import io.github.excu101.pluginsystem.utils.ActionScopeImpl
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory

data class DrawerItem(
    override val value: Action,
) : Item<Action> {

    constructor(title: String, icon: Drawable) : this(action(title, icon))

    override val id: Long = hashCode().toLong()

    override fun equals(other: Any?): Boolean = value == other

    override fun hashCode(): Int = value.hashCode()

    override val type: Int
        get() = ItemViewTypes.drawerItem

    companion object : ViewHolderFactory<DrawerItem> {
        override fun produceView(parent: ViewGroup): View {
            return DrawerItemView(parent.context)
        }
    }
}

fun ItemScope<Item<*>>.drawerItem(value: Action) {
    add(DrawerItem(value))
}


fun ItemScope<Item<*>>.drawerItem(scope: ActionScope.() -> Unit) {
    add(DrawerItem(ActionScopeImpl().apply(scope).toAction()))
}