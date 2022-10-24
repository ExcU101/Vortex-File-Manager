package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValuesDE() {
    Theme[fileListSearchActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Forschen")
    Theme[fileListMoreActionTitleKey] = io.github.excu101.pluginsystem.model.Text(value = "Mehr")
    Theme[fileListSortActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Sortieren")

    Theme[fileListFilesCountTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Dateien: $integerSpecifier")
    Theme[fileListDirectoriesCountTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Ordner: $integerSpecifier")

    Theme[fileListItemNameKey] = io.github.excu101.pluginsystem.model.Text(value = stringSpecifier)
    Theme[fileListItemsCountKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Objekte: $integerSpecifier")
    Theme[fileListItemCountKey] = io.github.excu101.pluginsystem.model.Text(value = "Ein objekt")
    Theme[fileListItemEmptyKey] = io.github.excu101.pluginsystem.model.Text(value = "Leer")

    Theme[vortexServiceConnectedKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Vortex Service ist verbunden!")
    Theme[vortexServiceDisconnectedKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Vortex Service ist getrennt!")
}