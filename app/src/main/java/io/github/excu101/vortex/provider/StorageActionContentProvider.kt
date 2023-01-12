package io.github.excu101.vortex.provider

import android.graphics.Color
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.dsl.withGroup
import io.github.excu101.vortex.ui.component.item.divider.divider
import io.github.excu101.vortex.ui.component.item.drawer.attrs
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyAudioFileActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyFilesActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyFoldersActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyImageFileActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyTextFileActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyVideoFileActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupFilterActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupOperationDefaultActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupOrderActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupSortActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupViewActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreInfoActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListOrderAscendingActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListOrderDescendingActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortCreationTimeActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortLastAccessTimeActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortLastModifiedTimeActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortNameActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortPathActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortSizeActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListViewGridActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListViewListActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationAddActionNewTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationAddBookmarkTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationCopyActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationCopyPathTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationCutActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationDeleteActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationOpenTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationRemoveBookmarkTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationRenameActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationSwapNamesActionTitleKey
import io.github.excu101.vortex.ui.icon.Icons
import io.github.excu101.vortex.utils.Config

interface StorageActionContentProvider {

    fun onSingleItem(
        item: PathItem,
    ): List<Item<*>>

    fun onSelectedItems(
        selected: List<PathItem>,
    ): List<Item<*>>

    fun onSortActions(): List<Item<*>>

    fun onMoreActions(): List<Item<*>>

}

fun StorageActionContentProvider(): StorageActionContentProvider {
    return object : StorageActionContentProvider {
        override fun onSingleItem(item: PathItem) = scope {
            withGroup(title = ThemeText(fileListGroupOperationDefaultActionTitleKey)) {
                if (item.isDirectory) {
                    drawerItem {
                        id = ViewIds.Storage.Menu.OpenId
                        title = ThemeText(storageListOperationOpenTitleKey)
                        icon = Icons.Rounded.FolderOpen
                    }
                    drawerItem {
                        id = ViewIds.Storage.Menu.AddNewId
                        title = ThemeText(storageListOperationAddActionNewTitleKey)
                        icon = Icons.Rounded.Add
                    }
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.RenameId
                    title = ThemeText(storageListOperationRenameActionTitleKey)
                    icon = Icons.Rounded.Edit
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.DeleteId
                    title = ThemeText(storageListOperationDeleteActionTitleKey)
                    icon = Icons.Rounded.Delete

                    attrs {
                        textColor = Color.RED
                        iconColor = Color.RED
                    }
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.CopyId
                    title = ThemeText(storageListOperationCopyActionTitleKey)
                    icon = Icons.Rounded.Copy
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.MoveId
                    title = ThemeText(storageListOperationCutActionTitleKey)
                    icon = Icons.Rounded.Cut
                }
            }

            divider()

            withGroup(title = "View") {
                drawerItem {
                    title = "Edit tags"
                    icon = Icons.Rounded.Edit
                }

                drawerItem {
                    if (item in StorageBookmarkProvider.items) {
                        id = ViewIds.Storage.Menu.RemoveBookmarkId
                        title = ThemeText(storageListOperationRemoveBookmarkTitleKey)
                        icon = Icons.Rounded.BookmarkRemove
                    } else {
                        id = ViewIds.Storage.Menu.AddBookmarkId
                        title = ThemeText(storageListOperationAddBookmarkTitleKey)
                        icon = Icons.Rounded.BookmarkAdd
                    }
                }
            }

            divider()

            withGroup(title = "Additional") {
                drawerItem {
                    id = ViewIds.Storage.Menu.CopyPathId
                    title = ThemeText(storageListOperationCopyPathTitleKey)
                    icon = Icons.Rounded.Copy
                }

                if (item.isDirectory) {
                    drawerItem {
                        id = ViewIds.Storage.Menu.AddWatcherId
                        title = "Add watcher"
                        icon = Icons.Rounded.Watch
                    }
                }

                drawerItem {
                    id = ViewIds.Storage.Menu.InfoId
                    title = ThemeText(fileListMoreInfoActionTitleKey)
                    icon = Icons.Rounded.Info
                }
            }
        }

        override fun onSelectedItems(selected: List<PathItem>): List<Item<*>> = scope {
            withGroup(title = ThemeText(fileListGroupOperationDefaultActionTitleKey)) {
                if (selected.size == 2) {
                    drawerItem {
                        title = ThemeText(storageListOperationSwapNamesActionTitleKey)
                        icon = Icons.Rounded.Swap
                    }
                }
                drawerItem {
                    title = ThemeText(storageListOperationCopyActionTitleKey)
                    icon = Icons.Rounded.Copy
                }
                drawerItem {
                    title = ThemeText(storageListOperationCutActionTitleKey)
                    icon = Icons.Rounded.Cut
                }
                drawerItem {
                    title = ThemeText(storageListOperationDeleteActionTitleKey)
                    icon = Icons.Rounded.Delete
                }
            }
            divider()
            withGroup(title = "Additional") {
                drawerItem {
                    title = "Show selected"
                }
                drawerItem {
                    title = "Deselect"
                }
            }
        }

        override fun onSortActions(): List<Item<*>> = scope {
            withGroup(title = ThemeText(fileListGroupViewActionTitleKey)) {
                drawerItem {
                    title = ThemeText(fileListViewListActionTitleKey)
                    icon = Icons.Rounded.ViewColumn
                }
                drawerItem {
                    title = ThemeText(fileListViewGridActionTitleKey)
                    icon = Icons.Rounded.ViewGrid
                }
            }
            withGroup(title = ThemeText(fileListGroupOrderActionTitleKey)) {
                drawerItem {
                    title = ThemeText(fileListOrderAscendingActionTitleKey)
                }
                drawerItem {
                    title = ThemeText(fileListOrderDescendingActionTitleKey)
                }
            }
            withGroup(title = ThemeText(fileListGroupSortActionTitleKey)) {
                drawerItem {
                    title = ThemeText(fileListSortNameActionTitleKey)
                }
                drawerItem {
                    title = ThemeText(fileListSortPathActionTitleKey)
                }
                drawerItem {
                    title = ThemeText(fileListSortSizeActionTitleKey)
                }
                drawerItem {
                    title = ThemeText(fileListSortLastModifiedTimeActionTitleKey)
                }
                drawerItem {
                    title = ThemeText(fileListSortLastAccessTimeActionTitleKey)
                }
                drawerItem {
                    title = ThemeText(fileListSortCreationTimeActionTitleKey)
                }
            }
            withGroup(title = ThemeText(fileListGroupFilterActionTitleKey)) {
                drawerItem {
                    title = ThemeText(fileListFilterOnlyFoldersActionTitleKey)
                    icon = Icons.Rounded.Folder
                }
                drawerItem {
                    title = ThemeText(fileListFilterOnlyFilesActionTitleKey)
                    icon = Icons.Rounded.File
                }
                drawerItem {
                    title = ThemeText(fileListFilterOnlyTextFileActionTitleKey)
                    icon = Icons.Rounded.Text
                }
                drawerItem {
                    title = ThemeText(fileListFilterOnlyAudioFileActionTitleKey)
                    icon = Icons.Rounded.Audio
                }
                drawerItem {
                    title = ThemeText(fileListFilterOnlyImageFileActionTitleKey)
                    icon = Icons.Rounded.Image
                }
                drawerItem {
                    title = ThemeText(fileListFilterOnlyVideoFileActionTitleKey)
                    icon = Icons.Rounded.Video
                }
            }
        }

        override fun onMoreActions(): List<Item<*>> = scope {
            withGroup(title = "Default") {
//                if (!isItemTrailFirst) {
//                    drawerItem {
//                        title = ThemeText(fileListMoreNavigateLeftActionTitleKey)
//                        icon = resources[R.drawable.ic_arrow_left_24]
//                    }
//                }
//                if (!isItemTrailLast) {
//                    drawerItem {
//                        title = ThemeText(fileListMoreNavigateRightActionTitleKey)
//                        icon = resources[R.drawable.ic_arrow_right_24]
//                    }
//                }
//                if (selectedCount <= 0) {
//                    drawerItem {
//                        title = ThemeText(fileListMoreSelectAllActionTitleKey)
//                        icon = resources[R.drawable.ic_select_all_24]
//                    }
//                } else {
//                    drawerItem {
//                        title = ThemeText(fileListMoreDeselectAllActionTitleKey)
//                        icon = resources[R.drawable.ic_deselect_all_24]
//                    }
//                }
                drawerItem {
                    title = ThemeText(fileListMoreInfoActionTitleKey)
                    icon = Icons.Rounded.Info
                }
            }
            if (Config.isDebug) {
                withGroup(title = "Developer") {
                    drawerItem {
                        title = "Shuffle list"
                    }
                    drawerItem {
                        title = "Add random-name directory"
                        icon = Icons.Rounded.Folder
                    }
                    drawerItem {
                        title = "Add random-name file"
                        icon = Icons.Rounded.File
                    }
                    drawerItem {
                        title = "Add random-name link"
                        icon = Icons.Rounded.Link
                    }
                    drawerItem {
                        title = "Get file system info"
                        icon = Icons.Rounded.Info
                    }
                }
            }
        }
    }
}