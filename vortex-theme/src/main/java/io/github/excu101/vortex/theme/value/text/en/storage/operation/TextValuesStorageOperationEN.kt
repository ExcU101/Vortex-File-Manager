package io.github.excu101.vortex.theme.value.text.en.storage.operation

import io.github.excu101.vortex.theme.Theme
import io.github.excu101.vortex.theme.key.storageListOperationAddActionNewTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationCopyActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationCopyPathTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationCutActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationDeleteActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationOpenTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationRenameActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationSwapNamesActionTitleKey
import io.github.excu101.vortex.theme.model.Text

fun initStorageOperationValueEN() {
    Theme[storageListOperationOpenTitleKey] = Text(value = "Open")
    Theme[storageListOperationCopyPathTitleKey] = Text(value = "Copy path")
    Theme[storageListOperationSwapNamesActionTitleKey] = Text(value = "Swap names")
    Theme[storageListOperationDeleteActionTitleKey] = Text(value = "Delete")
    Theme[storageListOperationRenameActionTitleKey] = Text(value = "Rename")
    Theme[storageListOperationCopyActionTitleKey] = Text(value = "Copy")
    Theme[storageListOperationCutActionTitleKey] = Text(value = "Cut")
    Theme[storageListOperationAddActionNewTitleKey] = Text(value = "Add new")
}