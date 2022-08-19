package io.github.excu101.vortex.ui.theme

import io.github.excu101.vortex.data.Text
import io.github.excu101.vortex.ui.theme.key.fileListDirectoriesCountKey
import io.github.excu101.vortex.ui.theme.key.fileListFilesCountKey

fun defaultTexts() {
    Theme[fileListFilesCountKey] = Text(value = "Files: %s")
    Theme[fileListDirectoriesCountKey] = Text(value = "Folders: %s")
}