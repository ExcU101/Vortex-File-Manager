package io.github.excu101.vortex.ui.component.theme.value.text.en

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.theme.value.text.en.navigation.initNavigationValuesEN
import io.github.excu101.vortex.ui.component.theme.value.text.en.storage.item.initStorageItemValuesEN

fun initVortexTextValuesEN() {
    initNavigationValuesEN()
    initStorageItemValuesEN()
    Theme[fileListLoadingInitiatingTitleKey] = Text(value = "Initiating...")
    Theme[fileListLoadingNavigatingTitleKey] = Text(value = "Navigating to $stringSpecifier")

    // Warning
    Theme[fileListWarningEmptyTitleKey] = Text(value = "$stringSpecifier is empty")

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

    // Storage list : Create dialog
    Theme[fileListCreateDialogNameHintTitleKey] = Text(value = "Enter name")
    Theme[fileListCreateDialogPathHintTitleKey] = Text(value = "Enter path")

    // Storage list : Drawer Groups
    Theme[fileListGroupViewActionTitleKey] = Text(value = "View")
    Theme[fileListGroupOrderActionTitleKey] = Text(value = "Order")
    Theme[fileListGroupSortActionTitleKey] = Text(value = "Sort")
    Theme[fileListGroupFilterActionTitleKey] = Text(value = "Filter")

    // Storage list : Drawer Groups : View
    Theme[fileListViewListActionTitleKey] = Text(value = "Column")
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
    Theme[fileListFilterOnlyApplicationFileActionTitleKey] = Text(value = "Only application files")
    Theme[fileListFilterOnlyAudioFileActionTitleKey] = Text(value = "Only audio files")
    Theme[fileListFilterOnlyImageFileActionTitleKey] = Text(value = "Only image files")
    Theme[fileListFilterOnlyVideoFileActionTitleKey] = Text(value = "Only video files")
    Theme[fileListFilterOnlyTextFileActionTitleKey] = Text(value = "Only text files")

    // More
    Theme[fileListGroupMoreActionTitleKey] = Text(value = "Default ($stringSpecifier)")

    Theme[fileListMoreSelectAllActionTitleKey] = Text(value = "Select all")
    Theme[fileListMoreDeselectAllActionTitleKey] = Text(value = "Deselect all")
    Theme[fileListMoreInfoActionTitleKey] = Text(value = "Info")
    Theme[fileListMoreNavigateLeftActionTitleKey] = Text(value = "Navigate left")
    Theme[fileListMoreNavigateRightActionTitleKey] = Text(value = "Navigate right")

    Theme[fileListFilesCountTitleKey] = Text(value = "Files: $integerSpecifier")
    Theme[fileListDirectoriesCountTitleKey] = Text(value = "Folders: $integerSpecifier")
    Theme[fileListSelectionTitleKey] = Text(value = "Selected $integerSpecifier")

    Theme[fileListFilesCountSectionKey] = Text(value = "Files ($integerSpecifier)")
    Theme[fileListDirectoriesCountSectionKey] = Text(value = "Folders ($integerSpecifier)")

    // Operation
    Theme[fileListOperationDeleteItemTitleKey] = Text(value = "Deleting $stringSpecifier")
    Theme[fileListOperationDeleteItemTitleKey] = Text(value = "Deletion performed")

    Theme[fileListGroupOperationDefaultActionTitleKey] = Text(value = "Default")

    Theme[storageListOperationAddBookmarkTitleKey] = Text(value = "Add to bookmarks")
    Theme[storageListOperationRemoveBookmarkTitleKey] = Text(value = "Remove from bookmarks")

    Theme[storageListOperationOpenTitleKey] = Text(value = "Open")
    Theme[storageListOperationCopyPathTitleKey] = Text(value = "Copy path")
    Theme[storageListOperationSwapNamesActionTitleKey] = Text(value = "Swap names")
    Theme[storageListOperationDeleteActionTitleKey] = Text(value = "Delete")
    Theme[storageListOperationRenameActionTitleKey] = Text(value = "Rename")
    Theme[storageListOperationCopyActionTitleKey] = Text(value = "Copy")
    Theme[storageListOperationCutActionTitleKey] = Text(value = "Cut")
    Theme[storageListOperationAddActionNewTitleKey] = Text(value = "Add new")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service is connected!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service is disconnected!")
}