package io.github.excu101.vortex.provider.main

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.provider.ActionProvider
import io.github.excu101.pluginsystem.provider.GroupActionProvider
import io.github.excu101.pluginsystem.utils.groupItem
import io.github.excu101.pluginsystem.utils.item
import io.github.excu101.vortex.R
import io.github.excu101.vortex.provider.ResourceProvider
import javax.inject.Inject
import kotlin.random.Random

open class MainAction @Inject constructor(
    private val resources: ResourceProvider,
) : ActionProvider, GroupActionProvider {

    override fun getActions(): List<Action> {
        return listOf()
    }

    override fun getGroups(): List<GroupAction> {
        return buildList {
            groupItem(title = "Vortex") {
                item {
                    title = "File Manager"
                    icon = resources[R.drawable.ic_folder_24]
                }
                item {
                    title = "Bookmarks"
                    icon = resources[R.drawable.ic_bookmark_24]
                }
            }

            groupItem(title = "Additional") {
                item {
                    title = "Switch theme"
                    icon = if (Random.nextBoolean()) {
                        resources[R.drawable.ic_light_mode_24]
                    } else {
                        resources[R.drawable.ic_dark_mode_24]
                    }
                }
                item {
                    title = "Settings"
                    icon = resources[R.drawable.ic_settings_24]
                }
            }
        }
    }
}