package io.github.excu101.vortex.ui.component.action

import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.data.header.TextHeaderItem
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory

class ActionAdapter : ItemAdapter<Item<*>>(
    ItemViewTypes.TEXT_HEADER to (TextHeaderItem as ViewHolderFactory<Item<*>>),
    ItemViewTypes.ACTION_HEADER to (ActionHeaderItem as ViewHolderFactory<Item<*>>)
) {

}