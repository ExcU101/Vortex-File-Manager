package io.github.excu101.vortex.ui.component.drawer

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.header.divider.DividerHeaderItem
import io.github.excu101.vortex.ui.component.header.icon.IconTextHeaderItem
import io.github.excu101.vortex.ui.component.header.text.TextHeaderItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory

class ActionAdapter : ItemAdapter<Item<*>>(
    ItemViewTypes.TEXT_HEADER to (TextHeaderItem as ViewHolderFactory<Item<*>>),
    ItemViewTypes.ICON_TEXT_HEADER to (IconTextHeaderItem as ViewHolderFactory<Item<*>>),
    ItemViewTypes.DIVIDER_HEADER to (DividerHeaderItem as ViewHolderFactory<Item<*>>)
) {

    private val selected = mutableListOf<Action>()

    fun replaceSelected(actions: List<Action>) {
        val changedActions = mutableListOf<Action>()
        val iterator = selected.iterator()
        while (iterator.hasNext()) {
            val action = iterator.next()
            if (action !in actions) {
                iterator.remove()
                changedActions.add(action)
            }
        }
        for (action in actions) {
            if (action !in selected) {
                selected.add(action)
                changedActions.add(action)
            }
        }
        for (action in changedActions) {
            val position = position(IconTextHeaderItem(action))
            notifyItemChanged(position)
        }
    }

    override fun isSelected(item: Item<*>): Boolean {
        if (item !is IconTextHeaderItem) return false
        return selected.contains(item.value)
    }

}