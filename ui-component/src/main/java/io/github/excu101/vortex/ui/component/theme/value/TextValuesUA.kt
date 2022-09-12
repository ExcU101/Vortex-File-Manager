package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValuesUA() {
    Theme[fileListSearchActionTitleKey] = Text(value = "Пошук")
    Theme[fileListMoreActionTitleKey] = Text(value = "Більше")
    Theme[fileListSortActionTitleKey] = Text(value = "Сортування")

    Theme[fileListFilesCountKey] = Text(value = "Файли: $specialSymbol")
    Theme[fileListDirectoriesCountKey] = Text(value = "Папки: $specialSymbol")

    Theme[fileListItemsCountKey] = Text(value = "Об'єкти: $specialSymbol")
    Theme[fileListItemCountKey] = Text(value = "Один об'єкт")
    Theme[fileListItemEmptyKey] = Text(value = "Пусто")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service підключено!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service відключено!")
}