package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValuesEN() {
    Theme[fileListLoadingInitiatingTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Initiating...")
    Theme[fileListLoadingNavigatingTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Navigating to $stringSpecifier")

    // Warning
    Theme[fileListWarningEmptyTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "$stringSpecifier is empty")

    // Warning : Permission
    Theme[fileListWarningFullStorageAccessTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "App requires")
    Theme[fileListWarningStorageAccessTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "App requires")
    Theme[fileListWarningNotificationAccessTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "App requires")

    // Warning : Action
    Theme[fileListWarningFullStorageAccessActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(
            value = "Provide full storage access"
        )
    Theme[fileListWarningStorageAccessActionTitleKey] = io.github.excu101.pluginsystem.model.Text(
        value = "Provide storage access"
    )
    Theme[fileListWarningNotificationAccessActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(
            value = "Provide notifications access"
        )

    // Bar
    Theme[fileListSearchActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Search")
    Theme[fileListMoreActionTitleKey] = io.github.excu101.pluginsystem.model.Text(value = "More")
    Theme[fileListSortActionTitleKey] = io.github.excu101.pluginsystem.model.Text(value = "Sort")

    // Storage list : Create dialog
    Theme[fileListCreateDialogNameHintTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Enter name")
    Theme[fileListCreateDialogPathHintTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Enter path")

    // Trail
    Theme[fileListTrailCopyPathActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Copy path")

    // Storage list : Drawer Groups
    Theme[fileListGroupViewActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "View")
    Theme[fileListGroupOrderActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Order")
    Theme[fileListGroupSortActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Sort")
    Theme[fileListGroupFilterActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Filter")

    // Storage list : Drawer Groups : View
    Theme[fileListViewColumnActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Column")
    Theme[fileListViewGridActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Grid")

    // Storage list : Drawer Groups : View
    Theme[fileListOrderAscendingActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Ascending")
    Theme[fileListOrderDescendingActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Descending")

    // Storage list : Drawer Groups : Sort
    Theme[fileListSortNameActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Name")
    Theme[fileListSortPathActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Path")
    Theme[fileListSortSizeActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Size")
    Theme[fileListSortLastModifiedTimeActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Last modified time")
    Theme[fileListSortLastAccessTimeActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Last access time")
    Theme[fileListSortCreationTimeActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Creation time")

    // Filter
    Theme[fileListFilterOnlyFoldersActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Only folders")
    Theme[fileListFilterOnlyFilesActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Only files")

    // More
    Theme[fileListGroupMoreActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Default ($stringSpecifier)")

    Theme[fileListMoreSelectAllActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Select all")
    Theme[fileListMoreDeselectAllActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Deselect all")
    Theme[fileListMoreInfoActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Info")
    Theme[fileListMoreNavigateLeftActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Navigate left")
    Theme[fileListMoreNavigateRightActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Navigate right")

    Theme[fileListFilesCountTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Files: $integerSpecifier")
    Theme[fileListDirectoriesCountTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Folders: $integerSpecifier")
    Theme[fileListSelectionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Selected $integerSpecifier")

    Theme[fileListFilesCountSectionKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Files ($integerSpecifier)")
    Theme[fileListDirectoriesCountSectionKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Folders ($integerSpecifier)")

    Theme[fileListItemMimeTypeApplicationKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Application")
    Theme[fileListItemMimeTypeImageKey] = io.github.excu101.pluginsystem.model.Text(value = "Image")
    Theme[fileListItemMimeTypeVideoKey] = io.github.excu101.pluginsystem.model.Text(value = "Video")
    Theme[fileListItemMimeTypeAudioKey] = io.github.excu101.pluginsystem.model.Text(value = "Audio")
    Theme[fileListItemMimeTypeTextKey] = io.github.excu101.pluginsystem.model.Text(value = "Text")

    // Item : Size
    Theme[fileListItemSizeBKey] = io.github.excu101.pluginsystem.model.Text(value = "B")
    Theme[fileListItemSizeKiBKey] = io.github.excu101.pluginsystem.model.Text(value = "KB")
    Theme[fileListItemSizeMiBKey] = io.github.excu101.pluginsystem.model.Text(value = "MB")
    Theme[fileListItemSizeGiBKey] = io.github.excu101.pluginsystem.model.Text(value = "GB")
    Theme[fileListItemSizeTiBKey] = io.github.excu101.pluginsystem.model.Text(value = "TB")
    Theme[fileListItemSizePiBKey] = io.github.excu101.pluginsystem.model.Text(value = "PB")
    Theme[fileListItemSizeEiBKey] = io.github.excu101.pluginsystem.model.Text(value = "EB")
    Theme[fileListItemSizeZiBKey] = io.github.excu101.pluginsystem.model.Text(value = "ZB")
    Theme[fileListItemSizeYiBKey] = io.github.excu101.pluginsystem.model.Text(value = "YB")

    // Item
    Theme[fileListItemNameKey] = io.github.excu101.pluginsystem.model.Text(value = stringSpecifier)
    Theme[fileListItemSizeKey] = io.github.excu101.pluginsystem.model.Text(value = stringSpecifier)
    Theme[fileListItemInfoSeparatorKey] = io.github.excu101.pluginsystem.model.Text(value = " | ")

    // Item : Directory Content
    Theme[fileListItemsCountKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Items $integerSpecifier")
    Theme[fileListItemCountKey] = io.github.excu101.pluginsystem.model.Text(value = "One item")
    Theme[fileListItemEmptyKey] = io.github.excu101.pluginsystem.model.Text(value = "Empty")

    // Operation
    Theme[fileListOperationDeleteItemTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Deleting $stringSpecifier")
    Theme[fileListOperationDeleteItemTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Deletion performed")

    Theme[fileListGroupOperationDefaultActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Default")

    Theme[fileListOperationSwapNamesActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Swap names")
    Theme[fileListOperationDeleteActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Delete")
    Theme[fileListOperationRenameActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Rename")
    Theme[fileListOperationCopyActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Copy")
    Theme[fileListOperationCutActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Cut")
    Theme[fileListOperationAddActionNewTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Add new")

    Theme[vortexServiceConnectedKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Vortex Service is connected!")
    Theme[vortexServiceDisconnectedKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Vortex Service is disconnected!")
}