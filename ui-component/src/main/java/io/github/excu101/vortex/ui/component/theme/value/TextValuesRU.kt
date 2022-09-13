package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValuesRU() {
    Theme[fileListSearchActionTitleKey] = Text(value = "Поиск")
    Theme[fileListMoreActionTitleKey] = Text(value = "Больше")
    Theme[fileListSortActionTitleKey] = Text(value = "Сортировка")

    Theme[fileListFilesCountKey] = Text(value = "Файлы: $specialSymbol")
    Theme[fileListDirectoriesCountKey] = Text(value = "Папки: $specialSymbol")

    Theme[fileListItemNameKey] = Text(value = specialSymbol)
    Theme[fileListItemsCountKey] = Text(value = "Объекты: $specialSymbol")
    Theme[fileListItemCountKey] = Text(value = "Один объект")
    Theme[fileListItemEmptyKey] = Text(value = "Пусто")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service подключен!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service отключен!")
}