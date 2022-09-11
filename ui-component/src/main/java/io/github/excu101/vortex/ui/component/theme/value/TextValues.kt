package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValues() {
    Theme[fileListSearchActionTitleKey] = Text(value = "Search")
    Theme[fileListMoreActionTitleKey] = Text(value = "More")
    Theme[fileListSortActionTitleKey] = Text(value = "Sort")

    Theme[fileListFilesCountKey] = Text(value = "Files: $specialSymbol")
    Theme[fileListDirectoriesCountKey] = Text(value = "Folders: $specialSymbol")

    Theme[fileListItemsCountKey] = Text(value = "Items $specialSymbol")
    Theme[fileListItemCountKey] = Text(value = "One item")
    Theme[fileListItemEmptyKey] = Text(value = "Empty")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service is connected!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service is disconnected!")
}