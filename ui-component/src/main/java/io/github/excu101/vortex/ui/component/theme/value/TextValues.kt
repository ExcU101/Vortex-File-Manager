package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValues() {
    Theme[fileListFilesCountKey] = Text(value = "Files: $specialSymbol")
    Theme[fileListDirectoriesCountKey] = Text(value = "Folders: $specialSymbol")

    Theme[fileListItemsCountKey] = Text(value = "Items $specialSymbol")
    Theme[fileListItemCountKey] = Text(value = "One item")
    Theme[fileListItemEmptyKey] = Text(value = "Empty")
}