package io.github.excu101.vortex.provider

import io.github.excu101.manager.ui.theme.ThemeText
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.dsl.withGroup
import io.github.excu101.vortex.ui.component.item.divider.divider
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupOperationDefaultActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreInfoActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreShowTasksActionTitleKey
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

interface StorageActionContentProvider {

    fun onSingleItem(
        item: PathItem,
    ): List<Item<*>>

    fun onSelectedItems(
        selected: List<PathItem>,
    ): List<Item<*>>

    fun onMoreActions(): List<Item<*>>

}

fun StorageActionContentProvider(
    bookmarks: StorageBookmarkProvider? = null,
): StorageActionContentProvider {
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

                if (bookmarks != null) {
                    drawerItem {
                        if (item in bookmarks.bookmarks.value) {
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

        override fun onMoreActions(): List<Item<*>> = scope {
            withGroup(title = "Default") {
                drawerItem {
                    id = ViewIds.Storage.Menu.InfoId
                    title = ThemeText(fileListMoreInfoActionTitleKey)
                    icon = Icons.Rounded.Info
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.ShowTasks
                    title = ThemeText(fileListMoreShowTasksActionTitleKey)
                    icon = Icons.Rounded.Tasks
                }
            }
        }
    }
}