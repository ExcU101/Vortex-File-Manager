package io.github.excu101.vortex.provider.main

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.provider.ActionProvider
import io.github.excu101.pluginsystem.provider.GroupActionProvider
import io.github.excu101.pluginsystem.utils.groupItem
import io.github.excu101.pluginsystem.utils.item
import io.github.excu101.vortex.ui.icon.Icons
import kotlin.random.Random

open class MainAction : ActionProvider, GroupActionProvider {

    override fun getActions(): List<Action> {
        return listOf()
    }

    override fun getGroups(): List<GroupAction> {
        return buildList {
            groupItem(title = "Vortex") {
                item {
                    title = "File Manager"
                    icon = Icons.Rounded.Folder
                }
                item {
                    title = "Bookmarks"
                    icon = Icons.Rounded.Bookmark
                }
            }

            groupItem(title = "Additional") {
                item {
                    title = "Switch theme"
                    icon = if (Random.nextBoolean()) {
                        Icons.Rounded.DarkMode
                    } else {
                        Icons.Rounded.LightMode
                    }
                }
                item {
                    title = "Settings"
                    icon = Icons.Rounded.Settings
                }
            }
        }
    }
}