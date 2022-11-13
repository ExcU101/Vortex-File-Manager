package io.github.excu101.vortex.ui.component.theme.key

import io.github.excu101.pluginsystem.annotation.StringSpecifier

// Format Specifiers
// %c - char
// %d - decimal int (base 10)
// %f - float
// %i - int (base 10)
// %o - octal (base 8)
// %s - string
// %u - unsigned decimal int
// %x - hexadecimal (base 16)
// %t - date/time
inline val charSpecifier
    get() = "%c"
inline val decimalSpecifier
    get() = "%d"
inline val floatSpecifier
    get() = "%f"
inline val integerSpecifier
    get() = "%d"
inline val octalSpecifier
    get() = "%o"
inline val stringSpecifier
    get() = "%s"
inline val unsignedIntegerSpecifier
    get() = "%u"
inline val hexadecimalSpecifier
    get() = "x"
inline val dateSpecifier
    get() = "%t"

const val fileListLoadingInitiatingTitleKey = "fileListLoadingInitiatingTitle"
const val fileListLoadingNavigatingTitleKey = "fileListLoadingNavigatingTitle"

// Warning
const val fileListWarningEmptyTitleKey = "fileListWarningEmptyTitle"

// Warning : Permission
const val fileListWarningFullStorageAccessTitleKey =
    "fileListWarningFullStorageAccessTitle"
const val fileListWarningStorageAccessTitleKey =
    "fileListWarningStorageAccessTitle"
const val fileListWarningNotificationAccessTitleKey =
    "fileListWarningNotificationAccessTitle"

// Warning : Action
const val fileListWarningFullStorageAccessActionTitleKey =
    "fileListWarningFullStorageAccessActionTitle"
const val fileListWarningStorageAccessActionTitleKey =
    "fileListWarningStorageAccessActionTitle"
const val fileListWarningNotificationAccessActionTitleKey =
    "fileListWarningNotificationAccessActionTitle"

// Bar
const val fileListSearchActionTitleKey = "fileListSearchActionTitle"
const val fileListMoreActionTitleKey = "fileListMoreActionTitle"
const val fileListSortActionTitleKey = "fileListSortActionTitle"

// Storage list : Create dialog
const val fileListCreateDialogNameHintTitleKey = "fileListCreateDialogNameHintTitle"
const val fileListCreateDialogPathHintTitleKey = "fileListCreateDialogPathHintTitle"

// Storage list : Trail
const val fileListTrailCopyPathActionTitleKey = "fileListTrailCopyPathActionTitle"

// Storage list : Drawer Groups
const val fileListGroupViewActionTitleKey = "fileListGroupViewActionTitle"
const val fileListGroupOrderActionTitleKey = "fileListGroupOrderActionTitleKey"
const val fileListGroupSortActionTitleKey = "fileListGroupSortActionTitle"
const val fileListGroupFilterActionTitleKey = "fileListGroupFilterActionTitle"

// Storage list : Drawer Groups : View
const val fileListViewListActionTitleKey = "fileListViewListActionTitle"
const val fileListViewGridActionTitleKey = "fileListViewGridActionTitle"

// Storage list : Drawer Groups : Order
const val fileListOrderAscendingActionTitleKey = "fileListOrderAscendingActionTitle"
const val fileListOrderDescendingActionTitleKey = "fileListOrderDescendingActionTitles"

// Storage list : Drawer Groups : Sort
const val fileListSortNameActionTitleKey = "fileListSortNameActionTitle"
const val fileListSortPathActionTitleKey = "fileListSortPathActionTitle"
const val fileListSortSizeActionTitleKey = "fileListSortSizeActionTitle"
const val fileListSortLastModifiedTimeActionTitleKey = "fileListSortLastModifiedTimeActionTitle"
const val fileListSortLastAccessTimeActionTitleKey = "fileListSortLastAccessTimeActionTitle"
const val fileListSortCreationTimeActionTitleKey = "fileListSortCreationTimeActionTitle"

// Storage list : Drawer Groups : Filter
const val fileListFilterOnlyFilesActionTitleKey = "fileListFilterOnlyFilesActionTitle"
const val fileListFilterOnlyFoldersActionTitleKey = "fileListFilterOnlyFoldersActionTitle"

// Storage list : Drawer Groups : More
const val fileListGroupMoreActionTitleKey = "fileListGroupMoreActionTitle"

const val fileListMoreSelectAllActionTitleKey = "fileListMoreSelectAllActionTitle"
const val fileListMoreDeselectAllActionTitleKey = "fileListMoreDeselectAllActionTitle"
const val fileListMoreInfoActionTitleKey = "fileListMoreInfoActionTitle"
const val fileListMoreNavigateLeftActionTitleKey = "fileListMoreNavigateLeftActionTitle"
const val fileListMoreNavigateRightActionTitleKey = "fileListMoreNavigateRightActionTitle"

// List
const val fileListFilesCountTitleKey = "fileListFilesCountTitle"
const val fileListDirectoriesCountTitleKey = "fileListDirectoriesCountTitle"
const val fileListSelectionTitleKey = "fileListSelectionTitle"

const val fileListFilesCountSectionKey = "fileListFilesCountSection"

@StringSpecifier
const val fileListDirectoriesCountSectionKey = "fileListDirectoriesCountSection"

// Operation
const val fileListOperationDeleteItemTitleKey = "fileListOperationDeleteItemTitleKey"
const val fileListOperationDeleteItemPerformedTitleKey = "fileListOperationDeleteItemPerformedTitle"

const val fileListGroupOperationDefaultActionTitleKey = "fileListGroupOperationDefaultActionTitle"

const val fileListOperationSwapNamesActionTitleKey = "fileListOperationSwapNamesActionTitle"
const val fileListOperationDeleteActionTitleKey = "fileListOperationDeleteActionTitle"
const val fileListOperationRenameActionTitleKey = "fileListOperationRenameActionTitle"
const val fileListOperationCopyActionTitleKey = "fileListOperationCopyActionTitle"
const val fileListOperationCutActionTitleKey = "fileListOperationCutActionTitle"
const val fileListOperationAddActionNewTitleKey = "fileListOperationAddNewActionTitle"

// Vortex Service
const val vortexServiceConnectedKey = "vortexServiceConnected"
const val vortexServiceDisconnectedKey = "vortexServiceDisconnected"