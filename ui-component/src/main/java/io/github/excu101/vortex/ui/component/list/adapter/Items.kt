package io.github.excu101.vortex.ui.component.list.adapter

import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.item.divider.DividerHeaderItem
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory

val DrawerViewHolderFactories = arrayOf(
    ItemViewTypes.DrawerItem to DrawerItem as ViewHolderFactory<Item<*>>,
    ItemViewTypes.TextItem to TextItem as ViewHolderFactory<Item<*>>,
    ItemViewTypes.DividerItem to DividerHeaderItem as ViewHolderFactory<Item<*>>,
)