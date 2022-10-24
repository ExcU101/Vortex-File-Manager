package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValuesUA() {
    Theme[fileListSearchActionTitleKey] = io.github.excu101.pluginsystem.model.Text(value = "Пошук")
    Theme[fileListMoreActionTitleKey] = io.github.excu101.pluginsystem.model.Text(value = "Більше")
    Theme[fileListSortActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Сортування")

    // Trail
    Theme[fileListTrailCopyPathActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Копіювати шлях")

    // Group
    Theme[fileListGroupViewActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Перегляд")
    Theme[fileListGroupSortActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Сортувати виділене")
    Theme[fileListGroupFilterActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Фільтрувати")

    // View
    Theme[fileListViewColumnActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Стовпець")
    Theme[fileListViewGridActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Сітка")

    // Sort
    Theme[fileListSortNameActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Ім'я")
    Theme[fileListSortSizeActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Розмір")
    Theme[fileListSortLastModifiedTimeActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Час останньої зміни")
    Theme[fileListSortLastAccessTimeActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Час останньго відкриття")
    Theme[fileListSortCreationTimeActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Час створення")

    // Filter
    Theme[fileListFilterOnlyFoldersActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Тільки папки")
    Theme[fileListFilterOnlyFilesActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Тільки файли")

    Theme[fileListFilesCountTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Файли: $integerSpecifier")
    Theme[fileListDirectoriesCountTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Папки: $integerSpecifier")
    Theme[fileListSelectionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Виділено $integerSpecifier")

    Theme[fileListFilesCountSectionKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Файли ($integerSpecifier)")
    Theme[fileListDirectoriesCountSectionKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Папки ($integerSpecifier)")

    Theme[fileListItemMimeTypeApplicationKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Додаток")
    Theme[fileListItemMimeTypeImageKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Зображення")
    Theme[fileListItemMimeTypeVideoKey] = io.github.excu101.pluginsystem.model.Text(value = "Відео")
    Theme[fileListItemMimeTypeAudioKey] = io.github.excu101.pluginsystem.model.Text(value = "Аудіо")
    Theme[fileListItemMimeTypeTextKey] = io.github.excu101.pluginsystem.model.Text(value = "Текст")

    // Item : Size
    Theme[fileListItemSizeBKey] = io.github.excu101.pluginsystem.model.Text(value = "Б")
    Theme[fileListItemSizeKiBKey] = io.github.excu101.pluginsystem.model.Text(value = "КБ")
    Theme[fileListItemSizeMiBKey] = io.github.excu101.pluginsystem.model.Text(value = "МБ")
    Theme[fileListItemSizeGiBKey] = io.github.excu101.pluginsystem.model.Text(value = "ГБ")
    Theme[fileListItemSizeTiBKey] = io.github.excu101.pluginsystem.model.Text(value = "ТБ")
    Theme[fileListItemSizePiBKey] = io.github.excu101.pluginsystem.model.Text(value = "ПБ")
    Theme[fileListItemSizeEiBKey] = io.github.excu101.pluginsystem.model.Text(value = "ЕБ")
    Theme[fileListItemSizeZiBKey] = io.github.excu101.pluginsystem.model.Text(value = "ЗБ")
    Theme[fileListItemSizeYiBKey] = io.github.excu101.pluginsystem.model.Text(value = "ЙБ")

    Theme[fileListItemNameKey] = io.github.excu101.pluginsystem.model.Text(value = stringSpecifier)
    Theme[fileListItemsCountKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Об'єкти: $integerSpecifier")
    Theme[fileListItemCountKey] = io.github.excu101.pluginsystem.model.Text(value = "Один об'єкт")
    Theme[fileListItemEmptyKey] = io.github.excu101.pluginsystem.model.Text(value = "Пусто")

    // Operation
    Theme[fileListGroupOperationDefaultActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Стандартні")

    Theme[fileListOperationDeleteActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Видалити")
    Theme[fileListOperationRenameActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Перейменувати")
    Theme[fileListOperationCopyActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Копіювати")
    Theme[fileListOperationCutActionTitleKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Вирізати")

    Theme[vortexServiceConnectedKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Vortex Service підключено!")
    Theme[vortexServiceDisconnectedKey] =
        io.github.excu101.pluginsystem.model.Text(value = "Vortex Service відключено!")
}