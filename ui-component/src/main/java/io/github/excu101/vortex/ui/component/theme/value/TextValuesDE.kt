package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValuesDE() {
    Theme[fileListSearchActionTitleKey] = Text(value = "Forschen")
    Theme[fileListMoreActionTitleKey] = Text(value = "Mehr")
    Theme[fileListSortActionTitleKey] = Text(value = "Sortieren")

    Theme[fileListFilesCountKey] = Text(value = "Dateien: $specialSymbol")
    Theme[fileListDirectoriesCountKey] = Text(value = "Ordner: $specialSymbol")

    Theme[fileListItemsCountKey] = Text(value = "Objekte: $specialSymbol")
    Theme[fileListItemCountKey] = Text(value = "Ein objekt")
    Theme[fileListItemEmptyKey] = Text(value = "Leer")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service ist verbunden!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service ist getrennt!")
}