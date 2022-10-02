package io.github.excu101.vortex.provider.storage

import androidx.annotation.DrawableRes
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.groupItem
import io.github.excu101.pluginsystem.utils.item
import io.github.excu101.vortex.R
import io.github.excu101.vortex.provider.ActionProvider
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.screen.list.StorageScreenContentState
import javax.inject.Inject

class StorageActionProvider @Inject constructor(
    private val resources: ResourceProvider,
) : ActionProvider() {

    private fun action(title: String, @DrawableRes icon: Int): Action {
        return Action(title = title, icon = resources[icon])
    }

    fun trailActions(): List<GroupAction> = buildList {
        groupItem(title = "Default") {
            item {
                title = ThemeText(fileListTrailCopyPathActionTitleKey)
                icon = resources[R.drawable.ic_copy_24]
            }
            item {
                title = ThemeText(fileListOperationDeleteActionTitleKey)
                icon = resources[R.drawable.ic_delete_24]
            }
        }
    }

    fun onItems(
        state: StorageScreenContentState,
    ): List<GroupAction> = buildList {
        groupItem(title = ThemeText(fileListGroupOperationDefaultActionTitleKey)) {
            if (state.selectedCount == 1) {
                item {
                    title = ThemeText(fileListOperationRenameActionTitleKey)
                    icon = resources[R.drawable.ic_edit_24]
                }
            }
            if (state.selectedCount == 2) {
                item {
                    title = ThemeText(fileListOperationSwapNamesActionTitleKey)
                    icon = resources[R.drawable.ic_swap_24]
                }
            }
            item {
                title = ThemeText(fileListOperationCopyActionTitleKey)
                icon = resources[R.drawable.ic_copy_24]
            }
            item {
                title = ThemeText(fileListOperationCutActionTitleKey)
                icon = resources[R.drawable.ic_cut_24]
            }
            item {
                title = ThemeText(fileListOperationDeleteActionTitleKey)
                icon = resources[R.drawable.ic_delete_24]
            }
        }
    }

    fun sortActions(): List<GroupAction> {
        return buildList {
            groupItem(title = ThemeText(fileListGroupViewActionTitleKey)) {
                item {
                    title = ThemeText(fileListViewColumnActionTitleKey)
                    icon = resources[R.drawable.ic_view_column_24]
                }
            }
            groupItem(title = ThemeText(fileListGroupSortActionTitleKey)) {
                item {
                    title = ThemeText(fileListSortNameActionTitleKey)
                }
                item {
                    title = ThemeText(fileListSortPathActionTitleKey)
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
            groupItem(title = ThemeText(fileListGroupFilterActionTitleKey)) {
                item {
                    title = ThemeText(fileListFilterOnlyFoldersActionTitleKey)
                    icon = resources[R.drawable.ic_folder_24]
                }
                item {
                    title = ThemeText(fileListFilterOnlyFilesActionTitleKey)
                    icon = resources[R.drawable.ic_file_24]
                }
            }
        }
    }

    fun moreActions(
        state: StorageScreenContentState,
    ): List<GroupAction> {
        return buildList {
            groupItem(title = "Default") {
                if (!state.isEmpty) {
                    if (state.isSelectedEmpty) {
                        item {
                            title = ThemeText(fileListMoreSelectAllActionTitleKey)
                            icon = resources[R.drawable.ic_select_all_24]
                        }
                    }
                    if (state.selected.containsAll(state.content)) {
                        item {
                            title = ThemeText(fileListMoreDeselectAllActionTitleKey)
                            icon = resources[R.drawable.ic_deselect_all_24]
                        }
                    }
                }
                if (!state.isSelectedEmpty && !state.isSelectedContainsContent) {
                    item {
                        title = "Select other"
                    }
                    item {
                        title = "Deselect selected"
                    }
                }
                if (!state.isItemTrailFirst)
                    item {
                        title = ThemeText(fileListMoreNavigateLeftActionTitleKey)
                        icon = resources[R.drawable.ic_arrow_left_24]
                    }
                if (!state.isItemTrailLast) {
                    item {
                        title = ThemeText(fileListMoreNavigateRightActionTitleKey)
                        icon = resources[R.drawable.ic_arrow_right_24]
                    }
                }
                item {
                    title = ThemeText(fileListMoreInfoActionTitleKey)
                    icon = resources[R.drawable.ic_info_24]
                }
            }
        }
    }

    override fun defaultBarActions(): List<Action> {
        return listOf(
            action(title = ThemeText(fileListMoreActionTitleKey), icon = R.drawable.ic_more_24),
            action(title = ThemeText(fileListSortActionTitleKey), icon = R.drawable.ic_filter_24),
            action(title = ThemeText(fileListSearchActionTitleKey), icon = R.drawable.ic_search_24),
        )
    }

    override fun defaultDrawerGroups(
        isDark: Boolean,
    ): List<GroupAction> {
        return buildList {
            groupItem(title = "Default actions") {
                item {
                    title = "Switch theme"
                    icon = if (isDark) {
                        resources[R.drawable.ic_light_mode_24]
                    } else {
                        resources[R.drawable.ic_dark_mode_24]
                    }
                }
            }
        }
    }

}