package io.github.excu101.vortex.theme.value

import io.github.excu101.vortex.theme.Theme
import io.github.excu101.vortex.theme.key.fileListDirectoriesCountTitleKey
import io.github.excu101.vortex.theme.key.fileListFilesCountTitleKey
import io.github.excu101.vortex.theme.key.fileListMoreActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSearchActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSortActionTitleKey
import io.github.excu101.vortex.theme.key.integerSpecifier
import io.github.excu101.vortex.theme.key.stringSpecifier
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemCountKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemEmptyKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemNameKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemsCountKey
import io.github.excu101.vortex.theme.key.vortexServiceConnectedKey
import io.github.excu101.vortex.theme.key.vortexServiceDisconnectedKey
import io.github.excu101.vortex.theme.model.Text

fun initVortexTextValuesDE() {
    Theme[fileListSearchActionTitleKey] = Text(value = "Forschen")
    Theme[fileListMoreActionTitleKey] = Text(value = "Mehr")
    Theme[fileListSortActionTitleKey] = Text(value = "Sortieren")

    Theme[fileListFilesCountTitleKey] = Text(value = "Dateien: $integerSpecifier")
    Theme[fileListDirectoriesCountTitleKey] = Text(value = "Ordner: $integerSpecifier")

    Theme[fileListItemNameKey] = Text(value = stringSpecifier)
    Theme[fileListItemsCountKey] = Text(value = "Objekte: $integerSpecifier")
    Theme[fileListItemCountKey] = Text(value = "Ein objekt")
    Theme[fileListItemEmptyKey] = Text(value = "Leer")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service ist verbunden!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service ist getrennt!")
}