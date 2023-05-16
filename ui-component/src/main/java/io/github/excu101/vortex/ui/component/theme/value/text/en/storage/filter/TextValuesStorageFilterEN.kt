package io.github.excu101.vortex.ui.component.theme.value.text.en.storage.filter

import io.github.excu101.manager.model.Text
import io.github.excu101.manager.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyApplicationFileActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyAudioFileActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyFilesActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyFoldersActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyImageFileActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyTextFileActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilterOnlyVideoFileActionTitleKey

fun initStorageFilterEN() {
    Theme[fileListFilterOnlyFoldersActionTitleKey] = Text(value = "Only folders")
    Theme[fileListFilterOnlyFilesActionTitleKey] = Text(value = "Only files")
    Theme[fileListFilterOnlyApplicationFileActionTitleKey] = Text(value = "Only application files")
    Theme[fileListFilterOnlyAudioFileActionTitleKey] = Text(value = "Only audio files")
    Theme[fileListFilterOnlyImageFileActionTitleKey] = Text(value = "Only image files")
    Theme[fileListFilterOnlyVideoFileActionTitleKey] = Text(value = "Only video files")
    Theme[fileListFilterOnlyTextFileActionTitleKey] = Text(value = "Only text \uD83D\uDC7F files")
}