package io.github.excu101.vortex.data

import android.view.View
import android.view.ViewGroup
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.drawer.NavigationDrawerHeaderItemView
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.utils.navigationDrawerHeader

object NavigationDrawerHeaderItem : Item<Unit>, ViewHolderFactory<NavigationDrawerHeaderItem> {
    override val value: Unit = Unit
    override val id: Long = Long.MIN_VALUE + 1
    override val type: Int
        get() = ItemViewTypes.navigationDrawerHeader

    override fun produceView(parent: ViewGroup): View {
        return NavigationDrawerHeaderItemView(parent.context)
    }
}