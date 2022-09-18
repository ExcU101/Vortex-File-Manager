package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexTextValuesUA() {
    Theme[fileListSearchActionTitleKey] = Text(value = "Пошук")
    Theme[fileListMoreActionTitleKey] = Text(value = "Більше")
    Theme[fileListSortActionTitleKey] = Text(value = "Сортування")

    // Trail
    Theme[fileListTrailCopyPathActionTitleKey] = Text(value = "Копіювати шлях")

    // Group
    Theme[fileListGroupViewActionTitleKey] = Text(value = "Перегляд")
    Theme[fileListGroupSortActionTitleKey] = Text(value = "Сортувати виділене")
    Theme[fileListGroupFilterActionTitleKey] = Text(value = "Фільтрувати")

    // View
    Theme[fileListViewColumnActionTitleKey] = Text(value = "Стовпець")
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

    Theme[fileListFilesCountTitleKey] = Text(value = "Файли: $specialSymbol")
    Theme[fileListDirectoriesCountTitleKey] = Text(value = "Папки: $specialSymbol")
    Theme[fileListSelectionTitleKey] = Text(value = "Виділено $specialSymbol")

    Theme[fileListFilesCountSectionKey] = Text(value = "Файли ($specialSymbol)")
    Theme[fileListDirectoriesCountSectionKey] = Text(value = "Папки ($specialSymbol)")

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

    Theme[fileListItemNameKey] = Text(value = specialSymbol)
    Theme[fileListItemsCountKey] = Text(value = "Об'єкти: $specialSymbol")
    Theme[fileListItemCountKey] = Text(value = "Один об'єкт")
    Theme[fileListItemEmptyKey] = Text(value = "Пусто")

    // Operation
    Theme[fileListGroupOperationDefaultActionTitleKey] = Text(value = "Стандартні")

    Theme[fileListOperationDeleteActionTitleKey] = Text(value = "Видалити")
    Theme[fileListOperationRenameActionTitleKey] = Text(value = "Перейменувати")
    Theme[fileListOperationCopyActionTitleKey] = Text(value = "Копіювати")
    Theme[fileListOperationCutActionTitleKey] = Text(value = "Вирізати")

    Theme[vortexServiceConnectedKey] = Text(value = "Vortex Service підключено!")
    Theme[vortexServiceDisconnectedKey] = Text(value = "Vortex Service відключено!")
}