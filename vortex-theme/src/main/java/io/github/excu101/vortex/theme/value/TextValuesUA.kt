package io.github.excu101.vortex.theme.value

import io.github.excu101.vortex.theme.Theme
import io.github.excu101.vortex.theme.key.fileListDirectoriesCountSectionKey
import io.github.excu101.vortex.theme.key.fileListDirectoriesCountTitleKey
import io.github.excu101.vortex.theme.key.fileListFilesCountSectionKey
import io.github.excu101.vortex.theme.key.fileListFilesCountTitleKey
import io.github.excu101.vortex.theme.key.fileListFilterOnlyFilesActionTitleKey
import io.github.excu101.vortex.theme.key.fileListFilterOnlyFoldersActionTitleKey
import io.github.excu101.vortex.theme.key.fileListGroupFilterActionTitleKey
import io.github.excu101.vortex.theme.key.fileListGroupOperationDefaultActionTitleKey
import io.github.excu101.vortex.theme.key.fileListGroupSortActionTitleKey
import io.github.excu101.vortex.theme.key.fileListGroupViewActionTitleKey
import io.github.excu101.vortex.theme.key.fileListMoreActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSearchActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSelectionTitleKey
import io.github.excu101.vortex.theme.key.fileListSortActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSortCreationTimeActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSortLastAccessTimeActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSortLastModifiedTimeActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSortNameActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSortSizeActionTitleKey
import io.github.excu101.vortex.theme.key.fileListViewGridActionTitleKey
import io.github.excu101.vortex.theme.key.fileListViewListActionTitleKey
import io.github.excu101.vortex.theme.key.integerSpecifier
import io.github.excu101.vortex.theme.key.storageListOperationCopyActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationCutActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationDeleteActionTitleKey
import io.github.excu101.vortex.theme.key.storageListOperationRenameActionTitleKey
import io.github.excu101.vortex.theme.key.stringSpecifier
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemCountKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemEmptyKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeApplicationKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeAudioKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeImageKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeTextKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeVideoKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemNameKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemSizeBKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemSizeEiBKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemSizeGiBKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemSizeKiBKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemSizeMiBKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemSizePiBKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemSizeTiBKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemSizeYiBKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemSizeZiBKey
import io.github.excu101.vortex.theme.key.text.storage.item.fileListItemsCountKey
import io.github.excu101.vortex.theme.key.vortexServiceConnectedKey
import io.github.excu101.vortex.theme.key.vortexServiceDisconnectedKey
import io.github.excu101.vortex.theme.model.Text

fun initVortexTextValuesUA() {
    Theme[fileListSearchActionTitleKey] = Text(value = "Пошук")
    Theme[fileListMoreActionTitleKey] = Text(value = "Більше")
    Theme[fileListSortActionTitleKey] = Text(value = "Сортування")

    // Group
    Theme[fileListGroupViewActionTitleKey] = Text(value = "Перегляд")
    Theme[fileListGroupSortActionTitleKey] = Text(value = "Сортувати виділене")
    Theme[fileListGroupFilterActionTitleKey] = Text(value = "Фільтрувати")

    // View
    Theme[fileListViewListActionTitleKey] = Text(value = "Стовпець")
    Theme[fileListViewGridActionTitleKey] = Text(value = "Сітка")

    // Sort
    Theme[fileListSortNameActionTitleKey] = Text(value = "Ім'я")
    Theme[fileListSortSizeActionTitleKey] = Text(value = "Розмір")
    Theme[fileListSortLastModifiedTimeActionTitleKey] = Text(value = "Час останньої зміни")
    Theme[fileListSortLastAccessTimeActionTitleKey] = Text(value = "Час останньго відкриття")
    Theme[fileListSortCreationTimeActionTitleKey] = Text(value = "Час створення")

    // Filter
    Theme[fileListFilterOnlyFoldersActionTitleKey] = Text(value = "Тільки папки")
    Theme[fileListFilterOnlyFilesActionTitleKey] = Text(value = "Тільки файли")

    Theme[fileListFilesCountTitleKey] = Text(value = "Файли: $integerSpecifier")
    Theme[fileListDirectoriesCountTitleKey] = Text(value = "Папки: $integerSpecifier")
    Theme[fileListSelectionTitleKey] = Text(value = "Виділено $integerSpecifier")

    Theme[fileListFilesCountSectionKey] = Text(value = "Файли ($integerSpecifier)")
    Theme[fileListDirectoriesCountSectionKey] = Text(value = "Папки ($integerSpecifier)")

    Theme[fileListItemMimeTypeApplicationKey] = Text(value = "Додаток")
    Theme[fileListItemMimeTypeImageKey] = Text(value = "Зображення")
    Theme[fileListItemMimeTypeVideoKey] = Text(value = "Відео")
    Theme[fileListItemMimeTypeAudioKey] = Text(value = "Аудіо")
    Theme[fileListItemMimeTypeTextKey] = Text(value = "Текст")

    // Item : Size
    Theme[fileListItemSizeBKey] = Text(value = "Б")
    Theme[fileListItemSizeKiBKey] = Text(value = "КБ")
    Theme[fileListItemSizeMiBKey] = Text(value = "МБ")
    Theme[fileListItemSizeGiBKey] = Text(value = "ГБ")
    Theme[fileListItemSizeTiBKey] = Text(value = "ТБ")
    Theme[fileListItemSizePiBKey] = Text(value = "ПБ")
    Theme[fileListItemSizeEiBKey] = Text(value = "ЕБ")
    Theme[fileListItemSizeZiBKey] = Text(value = "ЗБ")
    Theme[fileListItemSizeYiBKey] = Text(value = "ЙБ")

    Theme[fileListItemNameKey] = Text(value = stringSpecifier)
    Theme[fileListItemsCountKey] = Text(value = "Об'єкти: $integerSpecifier")
    Theme[fileListItemCountKey] = Text(value = "Один об'єкт")
    Theme[fileListItemEmptyKey] = Text(value = "Пусто")

    // Operation
    Theme[fileListGroupOperationDefaultActionTitleKey] = Text(value = "Стандартні")

    Theme[storageListOperationDeleteActionTitleKey] = Text(value = "Видалити")
    Theme[storageListOperationRenameActionTitleKey] = Text(value = "Перейменувати")
    Theme[storageListOperationCopyActionTitleKey] = Text(value = "Копіювати")
    Theme[storageListOperationCutActionTitleKey] = Text(value = "Вирізати")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service підключено!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service відключено!")
}