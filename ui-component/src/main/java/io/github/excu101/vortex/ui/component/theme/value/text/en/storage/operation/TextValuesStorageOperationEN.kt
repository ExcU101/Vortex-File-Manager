package io.github.excu101.vortex.ui.component.theme.value.text.en.storage.operation

import io.github.excu101.manager.model.Text
import io.github.excu101.manager.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationAddActionNewTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationCopyActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationCopyPathTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationCutActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationDeleteActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationOpenTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationRenameActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.storageListOperationSwapNamesActionTitleKey

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