package io.github.excu101.vortex.ui.component.list.adapter

import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.item.divider.DividerHeaderItem
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory

val DrawerViewHolderFactories: Array<Pair<Int, ViewHolderFactory<SuperItem>>> = arrayOf(
    ItemViewTypes.DrawerItem with DrawerItem,
    ItemViewTypes.TextItem with TextItem,
    ItemViewTypes.DividerItem with DividerHeaderItem,
)