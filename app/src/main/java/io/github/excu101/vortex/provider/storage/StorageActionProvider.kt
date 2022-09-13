package io.github.excu101.vortex.provider.storage

import androidx.annotation.DrawableRes
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.group
import io.github.excu101.pluginsystem.utils.groupItem
import io.github.excu101.pluginsystem.utils.item
import io.github.excu101.vortex.R
import io.github.excu101.vortex.provider.ActionProvider
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.ui.component.theme.key.*
import javax.inject.Inject

class StorageActionProvider @Inject constructor(
    private val resources: ResourceProvider,
) : ActionProvider() {

    private fun action(title: String, @DrawableRes icon: Int): Action {
        return Action(title = title, icon = resources.getDrawable(icon))
    }

    fun onSinglePathItem(): List<GroupAction> = buildList {
        groupItem(ThemeText(fileListGroupOperationDefaultActionTitleKey)) {
            item {
                title = ThemeText(fileListOperationRenameActionTitleKey)
                icon = resources.getDrawable(R.drawable.ic_edit_24)
            }
            item {
                title = ThemeText(fileListOperationCopyActionTitleKey)
                icon = resources.getDrawable(R.drawable.ic_copy_24)
            }
            item {
                title = ThemeText(fileListOperationCutActionTitleKey)
                icon = resources.getDrawable(R.drawable.ic_cut_24)
            }
            item {
                title = ThemeText(fileListOperationDeleteActionTitleKey)
                icon = resources.getDrawable(R.drawable.ic_delete_24)
            }
        }
    }

    fun onMultiPathItem(): List<GroupAction> = buildList {
        groupItem(title = ThemeText(fileListGroupOperationDefaultActionTitleKey)) {
            item {
                title = ThemeText(fileListOperationDeleteActionTitleKey)
                icon = resources.getDrawable(R.drawable.ic_delete_24)
            }
        }
    }

    fun sortActions(): List<GroupAction> {
        return buildList {
            groupItem(title = ThemeText(fileListGroupViewActionTitleKey)) {
                item {
                    title = ThemeText(fileListViewColumnActionTitleKey)
                    icon = resources.getDrawable(R.drawable.ic_view_column_24)
                }
            }
            groupItem(title = ThemeText(fileListGroupSortActionTitleKey)) {
                item {
                    title = ThemeText(fileListSortNameActionTitleKey)
                }
                item {
                    title = ThemeText(fileListSortSizeActionTitleKey)
                }
                item {
                    title = ThemeText(fileListSortLastModifiedTimeActionTitleKey)
                }
                item {
                    title = ThemeText(fileListSortLastAccessTimeActionTitleKey)
                }
                item {
                    title = ThemeText(fileListSortCreationTimeActionTitleKey)
                }
            }
            groupItem(title = ThemeText(fileListGroupSortActionTitleKey)) {
                item {
                    title = ThemeText(fileListFilterOnlyFoldersActionTitleKey)
                    icon = resources.getDrawable(R.drawable.ic_folder_24)
                }
                item {
                    title = ThemeText(fileListFilterOnlyFilesActionTitleKey)
                    icon = resources.getDrawable(R.drawable.ic_file_24)
                }
            }
        }
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