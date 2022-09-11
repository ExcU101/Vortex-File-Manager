package io.github.excu101.vortex.provider.storage

import androidx.annotation.DrawableRes
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.group
import io.github.excu101.vortex.R
import io.github.excu101.vortex.provider.ActionProvider
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortActionTitleKey
import javax.inject.Inject

class StorageActionProvider @Inject constructor(
    private val resources: ResourceProvider,
) : ActionProvider() {

    private fun action(title: String, @DrawableRes icon: Int): Action {
        return Action(title = title, icon = resources.getDrawable(icon))
    }

    fun onSinglePathItem(): List<GroupAction> = buildList {
        add(group(title = "Default") {
            item(action(title = "Rename", icon = R.drawable.ic_edit_24))
            item(action(title = "Copy", icon = R.drawable.ic_copy_24))
            item(action(title = "Cut", icon = R.drawable.ic_cut_24))
            item(action(title = "Delete", icon = R.drawable.ic_delete_24))
        })
    }

    fun onMultiPathItem(): List<GroupAction> = buildList {
        add(group(title = "Default") {
            item(action(title = "Delete", icon = R.drawable.ic_delete_24))
        })
    }

    override fun defaultBarActions(): List<Action> {
        return listOf(
            action(title = ThemeText(fileListMoreActionTitleKey), icon = R.drawable.ic_more_24),
            action(title = ThemeText(fileListSortActionTitleKey), icon = R.drawable.ic_filter_24),
            action(title = ThemeText(fileListMoreActionTitleKey), icon = R.drawable.ic_search_24),
        )
    }

    override fun defaultDrawerGroups(): List<GroupAction> {
        return buildList {
            add(group("Default actions") {
                item(action(title = "Switch theme", icon = R.drawable.ic_dark_mode_24))
            })
        }
    }

}