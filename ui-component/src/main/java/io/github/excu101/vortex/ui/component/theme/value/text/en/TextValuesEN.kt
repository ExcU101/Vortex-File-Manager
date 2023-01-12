package io.github.excu101.vortex.ui.component.theme.value.text.en

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.fileListCreateDialogNameHintTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListCreateDialogPathHintTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListDirectoriesCountSectionKey
import io.github.excu101.vortex.ui.component.theme.key.fileListDirectoriesCountTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilesCountSectionKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilesCountTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupFilterActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupMoreActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupOperationDefaultActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupOrderActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupSortActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListGroupViewActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListLoadingInitiatingTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListLoadingNavigatingTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreDeselectAllActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreInfoActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreNavigateLeftActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreNavigateRightActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreSelectAllActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListOperationDeleteItemTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListOrderAscendingActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListOrderDescendingActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSearchActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSelectionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortCreationTimeActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortLastAccessTimeActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortLastModifiedTimeActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortNameActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortPathActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortSizeActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListViewGridActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListViewListActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningEmptyTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningFullStorageAccessActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningFullStorageAccessTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningNotificationAccessActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningNotificationAccessTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningStorageAccessActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningStorageAccessTitleKey
import io.github.excu101.vortex.ui.component.theme.key.integerSpecifier
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationAddBookmarkTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationRemoveBookmarkTitleKey
import io.github.excu101.vortex.ui.component.theme.key.stringSpecifier
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceConnectedKey
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceDisconnectedKey
import io.github.excu101.vortex.ui.component.theme.value.text.en.navigation.initNavigationValuesEN
import io.github.excu101.vortex.ui.component.theme.value.text.en.storage.filter.initStorageFilterEN
import io.github.excu101.vortex.ui.component.theme.value.text.en.storage.item.initStorageItemValuesEN
import io.github.excu101.vortex.ui.component.theme.value.text.en.storage.operation.initStorageOperationValueEN

fun initVortexTextValuesEN() {
    initNavigationValuesEN()

    initStorageItemValuesEN()
    initStorageOperationValueEN()
    initStorageFilterEN()

    Theme[fileListLoadingInitiatingTitleKey] = Text(value = "Initiating...")
    Theme[fileListLoadingNavigatingTitleKey] = Text(value = "Navigating to $stringSpecifier")

    // Warning
    Theme[fileListWarningEmptyTitleKey] = Text(value = "$stringSpecifier is empty")

    // Warning : Permission
    Theme[fileListWarningFullStorageAccessTitleKey] = Text(
        value = "App requires full storage access.\n" +
                "Full storage access gives app full control of your file-system storage.\n" +
                "App guaranties your files can't be damaged.\n" +
                "Remember: Plugins can disallow this rule."
    )
    Theme[fileListWarningStorageAccessTitleKey] = Text(
        value = "App requires storage access.\n" +
                "Storage access gives app full control of your file-system storage.\n" +
                "App guaranties your files can't be damaged.\n" +
                "Remember: Plugins can disallow this rule.\n"
    )
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

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service is connected!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service is disconnected!")
}