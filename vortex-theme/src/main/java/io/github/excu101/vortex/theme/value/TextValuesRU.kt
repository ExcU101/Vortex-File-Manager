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

fun initVortexTextValuesRU() {
    Theme[fileListSearchActionTitleKey] = Text(value = "Поиск")
    Theme[fileListMoreActionTitleKey] = Text(value = "Больше")
    Theme[fileListSortActionTitleKey] =
        Text(value = "Сортировка")

    Theme[fileListFilesCountTitleKey] =
        Text(value = "Файлы: $integerSpecifier")
    Theme[fileListDirectoriesCountTitleKey] =
        Text(value = "Папки: $integerSpecifier")

    Theme[fileListItemNameKey] = Text(value = stringSpecifier)
    Theme[fileListItemsCountKey] =
        Text(value = "Объекты: $integerSpecifier")
    Theme[fileListItemCountKey] = Text(value = "Один объект")
    Theme[fileListItemEmptyKey] = Text(value = "Пусто")

    Theme[vortexServiceConnectedKey] =
        Text(value = "Vortex Service подключен!")
    Theme[vortexServiceDisconnectedKey] =
        Text(value = "Vortex Service отключен!")
}