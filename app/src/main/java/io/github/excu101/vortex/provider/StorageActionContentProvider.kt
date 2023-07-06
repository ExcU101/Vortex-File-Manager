package io.github.excu101.vortex.provider

import io.github.excu101.vortex.theme.ThemeText
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.dsl.withGroup
import io.github.excu101.vortex.ui.component.item.divider.divider
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.theme.key.fileListGroupOperationDefaultActionTitleKey
import io.github.excu101.vortex.theme.key.fileListMoreInfoActionTitleKey
import io.github.excu101.vortex.theme.key.fileListMoreShowTasksActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationCopyActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationCutActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationDeleteActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationSwapNamesActionTitleKey
import io.github.excu101.vortex.ui.icon.Icons

interface StorageActionContentProvider {

    fun onSingleItem(
        item: PathItem,
    ): List<Item<*>>

    fun onSelectedItems(
        count: Int,
    ): List<Item<*>>

    fun onMoreActions(): List<Item<*>>

}

fun StorageActionContentProvider(

): StorageActionContentProvider {
    return object : StorageActionContentProvider {
        override fun onSingleItem(item: PathItem) = scope {
            withGroup(title = io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListGroupOperationDefaultActionTitleKey)) {
                if (item.isDirectory) {
                    drawerItem {
                        id = ViewIds.Storage.Menu.OpenId
                        title =
                            io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationOpenTitleKey)
                        icon = Icons.Rounded.FolderOpen
                    }
                    drawerItem {
                        id = ViewIds.Storage.Menu.AddNewId
                        title =
                            io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationAddActionNewTitleKey)
                        icon = Icons.Rounded.Add
                    }
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.RenameId
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationRenameActionTitleKey)
                    icon = Icons.Rounded.Edit
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.DeleteId
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationDeleteActionTitleKey)
                    icon = Icons.Rounded.Delete
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.CopyId
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationCopyActionTitleKey)
                    icon = Icons.Rounded.Copy
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.MoveId
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationCutActionTitleKey)
                    icon = Icons.Rounded.Cut
                }
            }

            divider()

            withGroup(title = "View") {
                drawerItem {
                    title = "Edit tags"
                    icon = Icons.Rounded.Edit
                }
            }

            divider()

            withGroup(title = "Additional") {
                drawerItem {
                    id = ViewIds.Storage.Menu.CopyPathId
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationCopyPathTitleKey)
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
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListMoreInfoActionTitleKey)
                    icon = Icons.Rounded.Info
                }
            }
        }

        override fun onSelectedItems(count: Int): List<Item<*>> = scope {
            withGroup(title = io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListGroupOperationDefaultActionTitleKey)) {
                if (count == 2) {
                    drawerItem {
                        title =
                            io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationSwapNamesActionTitleKey)
                        icon = Icons.Rounded.Swap
                    }
                }
                drawerItem {
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationCopyActionTitleKey)
                    icon = Icons.Rounded.Copy
                }
                drawerItem {
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationCutActionTitleKey)
                    icon = Icons.Rounded.Cut
                }
                drawerItem {
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.storageListOperationDeleteActionTitleKey)
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
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListMoreInfoActionTitleKey)
                    icon = Icons.Rounded.Info
                }
                drawerItem {
                    id = ViewIds.Storage.Menu.ShowTasks
                    title =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListMoreShowTasksActionTitleKey)
                    icon = Icons.Rounded.Tasks
                }
            }
        }
    }
}