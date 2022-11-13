package io.github.excu101.vortex.provider.storage

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.action
import io.github.excu101.pluginsystem.utils.groupItem
import io.github.excu101.pluginsystem.utils.item
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.main.MainAction
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.utils.Config
import javax.inject.Inject

open class StorageActionProvider @Inject constructor(
    private val resources: ResourceProvider,
) : MainAction(resources) {

    open fun onSingleItem(
        item: PathItem,
    ) = mutableListOf<GroupAction>().apply {
        groupItem(title = ThemeText(fileListGroupOperationDefaultActionTitleKey)) {
            if (item.isDirectory) {
                item {
                    title = "Open"
                    icon = resources[R.drawable.ic_folder_open_24]
                }
                item {
                    title = "Add new"
                    icon = resources[R.drawable.ic_add_24]
                }
            }
            item {
                title = ThemeText(fileListOperationRenameActionTitleKey)
                icon = resources[R.drawable.ic_edit_24]
            }
            item {
                title = "Create symbolic link"
                icon = resources[R.drawable.ic_link_24]
            }
            item {
                title = ThemeText(fileListOperationDeleteActionTitleKey)
                icon = resources[R.drawable.ic_delete_24]
            }
            item {
                title = ThemeText(fileListOperationCopyActionTitleKey)
                icon = resources[R.drawable.ic_copy_24]
            }
            item {
                title = ThemeText(fileListOperationCutActionTitleKey)
                icon = resources[R.drawable.ic_cut_24]
            }
        }
        groupItem(title = "Additional") {
            item {
                title = "Copy path"
                icon = resources[R.drawable.ic_copy_24]
            }
            item {
                if (item in StorageBookmarkProvider.items) {
                    title = "Remove from Bookmarks"
                    icon = resources[R.drawable.ic_bookmark_remove_24]
                } else {
                    title = "Add to Bookmarks"
                    icon = resources[R.drawable.ic_bookmark_add_24]
                }
            }
            item {
                title = ThemeText(fileListMoreInfoActionTitleKey)
                icon = resources[R.drawable.ic_info_24]
            }
        }
    }

    open fun onSelectedItems(
        selected: List<PathItem>,
    ) = mutableListOf<GroupAction>().apply {
        groupItem(title = ThemeText(fileListGroupOperationDefaultActionTitleKey)) {
            if (selected.size == 2) {
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
        groupItem(title = "Additional") {
            item {
                title = "Show selected"
            }
            item {
                title = "Deselect"
            }
        }
    }

    open fun sortActions(): MutableList<GroupAction> = mutableListOf<GroupAction>().apply {
        groupItem(title = ThemeText(fileListGroupViewActionTitleKey)) {
            item {
                title = ThemeText(fileListViewListActionTitleKey)
                icon = resources[R.drawable.ic_view_column_24]
            }
            item {
                title = ThemeText(fileListViewGridActionTitleKey)
                icon = resources[R.drawable.ic_view_grid_24]
            }
        }
        groupItem(title = ThemeText(fileListGroupOrderActionTitleKey)) {
            item {
                title = ThemeText(fileListOrderAscendingActionTitleKey)
            }
            item {
                title = ThemeText(fileListOrderDescendingActionTitleKey)
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

    open fun moreActions(
        isItemTrailFirst: Boolean,
        isItemTrailLast: Boolean,
        selectedCount: Int,
    ): MutableList<GroupAction> = mutableListOf<GroupAction>().apply {
        groupItem(title = "Default") {
            if (StorageTaskManager.copyTasks.isNotEmpty()) {
                item {
                    title = "Open tasks"
                    icon = resources[R.drawable.ic_inbox_24]
                }
            }
//            if (!state.isEmpty) {
//                if (state.isSelectedEmpty) {
//                    item {
//                        title = ThemeText(fileListMoreSelectAllActionTitleKey)
//                        icon = resources[R.drawable.ic_select_all_24]
//                    }
//                }
//                if (state.isSelectedContainsContent) {
//                    item {
//                        title = ThemeText(fileListMoreDeselectAllActionTitleKey)
//                        icon = resources[R.drawable.ic_deselect_all_24]
//                    }
//                }
//            }
            if (!isItemTrailFirst) {
                item {
                    title = ThemeText(fileListMoreNavigateLeftActionTitleKey)
                    icon = resources[R.drawable.ic_arrow_left_24]
                }
            }
            if (!isItemTrailLast) {
                item {
                    title = ThemeText(fileListMoreNavigateRightActionTitleKey)
                    icon = resources[R.drawable.ic_arrow_right_24]
                }
            }
            if (selectedCount <= 0) {
                item {
                    title = ThemeText(fileListMoreSelectAllActionTitleKey)
                    icon = resources[R.drawable.ic_select_all_24]
                }
            } else {
                item {
                    title = ThemeText(fileListMoreDeselectAllActionTitleKey)
                    icon = resources[R.drawable.ic_deselect_all_24]
                }
            }
            item {
                title = ThemeText(fileListMoreInfoActionTitleKey)
                icon = resources[R.drawable.ic_info_24]
            }
        }
        if (Config.isDebug) {
            groupItem(title = "Developer") {
                item {
                    title = "Shuffle list"
                }
                item {
                    title = "Add random-name directory"
                }
                item {
                    title = "Add random-name file"
                }
                item {
                    title = "Add random-name link"
                }
                item {
                    title = "Get file system info"
                }
            }
        }
    }

    override fun getActions(): List<Action> {
        return buildList {
            action {
                title = ThemeText(fileListMoreActionTitleKey)
                icon = resources[R.drawable.ic_more_24]
            }
            action {
                title = ThemeText(fileListSortActionTitleKey)
                icon = resources[R.drawable.ic_filter_24]
            }
            action {
                title = ThemeText(fileListSearchActionTitleKey)
                icon = resources[R.drawable.ic_search_24]
            }
        }
    }

}