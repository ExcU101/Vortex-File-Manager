package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValuesEN() {
    Theme[fileListLoadingInitiatingTitleKey] = Text(value = "Initiating...")
    Theme[fileListLoadingNavigatingTitleKey] = Text(value = "Navigating to $specialSymbol...")

    // Warning
    Theme[fileListWarningEmptyTitleKey] = Text(value = "$specialSymbol is empty")

    // Warning : Permission
    Theme[fileListWarningFullStorageAccessTitleKey] = Text(value = "App requires")
    Theme[fileListWarningStorageAccessTitleKey] = Text(value = "App requires")
    Theme[fileListWarningNotificationAccessTitleKey] = Text(value = "App requires")

    // Warning : Action
    Theme[fileListWarningFullStorageAccessActionTitleKey] = Text(
        value = "Provide full storage access"
    )
    Theme[fileListWarningStorageAccessActionTitleKey] = Text(
        value = "Provide storage access"
    )
    Theme[fileListWarningNotificationAccessActionTitleKey] = Text(
        value = "Provide notifications access"
    )

    // Bar
    Theme[fileListSearchActionTitleKey] = Text(value = "Search")
    Theme[fileListMoreActionTitleKey] = Text(value = "More")
    Theme[fileListSortActionTitleKey] = Text(value = "Sort")

    // Trail
    Theme[fileListTrailCopyPathActionTitleKey] = Text(value = "Copy path")

    // Storage list : Drawer Groups
    Theme[fileListGroupViewActionTitleKey] = Text(value = "View")
    Theme[fileListGroupOrderActionTitleKey] = Text(value = "Order")
    Theme[fileListGroupSortActionTitleKey] = Text(value = "Sort")
    Theme[fileListGroupFilterActionTitleKey] = Text(value = "Filter")

    // Storage list : Drawer Groups : View
    Theme[fileListViewColumnActionTitleKey] = Text(value = "Column")
    Theme[fileListViewGridActionTitleKey] = Text(value = "Grid")

    // Storage list : Drawer Groups : View
    Theme[fileListOrderAscendingActionTitleKey] = Text(value = "Ascending")
    Theme[fileListOrderDescendingActionTitleKey] = Text(value = "Descending")

    // Storage list : Drawer Groups : Sort
    Theme[fileListSortNameActionTitleKey] = Text(value = "Name")
    Theme[fileListSortPathActionTitleKey] = Text(value = "Path")
    Theme[fileListSortSizeActionTitleKey] = Text(value = "Size")
    Theme[fileListSortLastModifiedTimeActionTitleKey] = Text(value = "Last modified time")
    Theme[fileListSortLastAccessTimeActionTitleKey] = Text(value = "Last access time")
    Theme[fileListSortCreationTimeActionTitleKey] = Text(value = "Creation time")

    // Filter
    Theme[fileListFilterOnlyFoldersActionTitleKey] = Text(value = "Only folders")
    Theme[fileListFilterOnlyFilesActionTitleKey] = Text(value = "Only files")

    // More
    Theme[fileListGroupMoreActionTitleKey] = Text(value = "Default ($specialSymbol)")

    Theme[fileListMoreSelectAllActionTitleKey] = Text(value = "Select all")
    Theme[fileListMoreDeselectAllActionTitleKey] = Text(value = "Deselect all")
    Theme[fileListMoreInfoActionTitleKey] = Text(value = "Info")
    Theme[fileListMoreNavigateLeftActionTitleKey] = Text(value = "Navigate left")
    Theme[fileListMoreNavigateRightActionTitleKey] = Text(value = "Navigate right")

    Theme[fileListFilesCountTitleKey] = Text(value = "Files: $specialSymbol")
    Theme[fileListDirectoriesCountTitleKey] = Text(value = "Folders: $specialSymbol")
    Theme[fileListSelectionTitleKey] = Text(value = "Selected $specialSymbol")

    Theme[fileListFilesCountSectionKey] = Text(value = "Files ($specialSymbol)")
    Theme[fileListDirectoriesCountSectionKey] = Text(value = "Folders ($specialSymbol)")

    Theme[fileListItemMimeTypeApplicationKey] = Text(value = "Application")
    Theme[fileListItemMimeTypeImageKey] = Text(value = "Image")
    Theme[fileListItemMimeTypeVideoKey] = Text(value = "Video")
    Theme[fileListItemMimeTypeAudioKey] = Text(value = "Audio")
    Theme[fileListItemMimeTypeTextKey] = Text(value = "Text")

    // Item : Size
    Theme[fileListItemSizeBKey] = Text(value = "B")
    Theme[fileListItemSizeKiBKey] = Text(value = "KB")
    Theme[fileListItemSizeMiBKey] = Text(value = "MB")
    Theme[fileListItemSizeGiBKey] = Text(value = "GB")
    Theme[fileListItemSizeTiBKey] = Text(value = "TB")
    Theme[fileListItemSizePiBKey] = Text(value = "PB")
    Theme[fileListItemSizeEiBKey] = Text(value = "EB")
    Theme[fileListItemSizeZiBKey] = Text(value = "ZB")
    Theme[fileListItemSizeYiBKey] = Text(value = "YB")

    // Item
    Theme[fileListItemNameKey] = Text(value = specialSymbol)
    Theme[fileListItemSizeKey] = Text(value = specialSymbol)

    // Item : Directory Content
    Theme[fileListItemsCountKey] = Text(value = "Items $specialSymbol")
    Theme[fileListItemCountKey] = Text(value = "One item")
    Theme[fileListItemEmptyKey] = Text(value = "Empty")

    // Operation
    Theme[fileListGroupOperationDefaultActionTitleKey] = Text(value = "Default")

    Theme[fileListOperationSwapNamesActionTitleKey] = Text(value = "Swap names")
    Theme[fileListOperationDeleteActionTitleKey] = Text(value = "Delete")
    Theme[fileListOperationRenameActionTitleKey] = Text(value = "Rename")
    Theme[fileListOperationCopyActionTitleKey] = Text(value = "Copy")
    Theme[fileListOperationCutActionTitleKey] = Text(value = "Cut")
    Theme[fileListOperationAddActionNewTitleKey] = Text(value = "Add new")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service is connected!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service is disconnected!")
}