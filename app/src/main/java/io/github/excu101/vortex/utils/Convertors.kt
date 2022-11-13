package io.github.excu101.vortex.utils

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.divider.divider
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.icon.ActionItem
import io.github.excu101.vortex.ui.component.item.icon.iconText
import io.github.excu101.vortex.ui.component.item.text.text
import io.github.excu101.vortex.ui.component.list.adapter.Item

fun List<GroupAction>.asItems(): List<Item<*>> {
    return scope {
        forEachIndexed { index, group ->
            if (index != 0) {
                divider()
            }
            text(group.name)
            addOther(group.actions.map { DrawerItem(it) })
        }
    }
}

fun List<Action>.asActions(): List<Item<*>> {
    return scope {
        forEach {
            iconText(value = it)
        }
    }
}