package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemCountKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemEmptyKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemNameKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemsCountKey

fun initVortexTextValuesRU() {
    Theme[fileListSearchActionTitleKey] = io.github.excu101.pluginsystem.model.Text(value = "Поиск")
    Theme[fileListMoreActionTitleKey] = io.github.excu101.pluginsystem.model.Text(value = "Больше")
    Theme[fileListSortActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Сортировка")

    Theme[fileListFilesCountTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Файлы: $integerSpecifier")
    Theme[fileListDirectoriesCountTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Папки: $integerSpecifier")

    Theme[fileListItemNameKey] = io.github.excu101.pluginsystem.model.Text(value = stringSpecifier)
    Theme[fileListItemsCountKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Объекты: $integerSpecifier")
    Theme[fileListItemCountKey] = io.github.excu101.pluginsystem.model.Text(value = "Один объект")
    Theme[fileListItemEmptyKey] = io.github.excu101.pluginsystem.model.Text(value = "Пусто")

    Theme[vortexServiceConnectedKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Vortex Service подключен!")
    Theme[vortexServiceDisconnectedKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Vortex Service отключен!")
}