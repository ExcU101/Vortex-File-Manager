package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValuesEN() {
    Theme[fileListSearchActionTitleKey] = Text(value = "Search")
    Theme[fileListMoreActionTitleKey] = Text(value = "More")
    Theme[fileListSortActionTitleKey] = Text(value = "Sort")

    // Trail
    Theme[fileListTrailCopyPathActionTitleKey] = Text(value = "Copy path")

    // Group
    Theme[fileListGroupViewActionTitleKey] = Text(value = "View")
    Theme[fileListGroupSortActionTitleKey] = Text(value = "Sort")
    Theme[fileListGroupFilterActionTitleKey] = Text(value = "Filter")

    // View
    Theme[fileListViewColumnActionTitleKey] = Text(value = "Column")
    Theme[fileListViewGridActionTitleKey] = Text(value = "Grid")

    // Sort
    Theme[fileListSortNameActionTitleKey] = Text(value = "Name")
    Theme[fileListSortSizeActionTitleKey] = Text(value = "Size")
    Theme[fileListSortLastModifiedTimeActionTitleKey] = Text(value = "Last modified time")
    Theme[fileListSortLastAccessTimeActionTitleKey] = Text(value = "Last access time")
    Theme[fileListSortCreationTimeActionTitleKey] = Text(value = "Creation time")

    // Filter
    Theme[fileListFilterOnlyFoldersActionTitleKey] = Text(value = "Only folders")
    Theme[fileListFilterOnlyFilesActionTitleKey] = Text(value = "Only files")

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

    Theme[fileListOperationDeleteActionTitleKey] = Text(value = "Delete")
    Theme[fileListOperationRenameActionTitleKey] = Text(value = "Rename")
    Theme[fileListOperationCopyActionTitleKey] = Text(value = "Copy")
    Theme[fileListOperationCutActionTitleKey] = Text(value = "Cut")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service is connected!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service is disconnected!")
}