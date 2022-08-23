package io.github.excu101.vortex.ui.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.fileListDirectoriesCountKey
import io.github.excu101.vortex.ui.theme.key.fileListFilesCountKey

fun initVortexTextValues() {
    Theme[fileListFilesCountKey] = Text(value = "Files: %s")
    Theme[fileListDirectoriesCountKey] = Text(value = "Folders: %s")
}