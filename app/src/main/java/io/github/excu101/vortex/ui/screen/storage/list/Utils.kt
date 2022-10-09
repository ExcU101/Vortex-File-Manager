package io.github.excu101.vortex.ui.screen.storage.list

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.EmptyDrawable
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.ui.component.theme.key.*

private val sorterTitles = mapOf(
    PathItemSorters.Name to ThemeText(fileListSortNameActionTitleKey),
    PathItemSorters.Path to ThemeText(fileListSortPathActionTitleKey),
    PathItemSorters.Size to ThemeText(fileListSortSizeActionTitleKey),
    PathItemSorters.LastAccessTime to ThemeText(fileListSortLastAccessTimeActionTitleKey),
    PathItemSorters.LastModifiedTime to ThemeText(fileListSortLastModifiedTimeActionTitleKey),
    PathItemSorters.CreationTime to ThemeText(fileListSortCreationTimeActionTitleKey),
)

private val orderTitles = mapOf(
    Order.ASCENDING to ThemeText(fileListOrderAscendingActionTitleKey),
    Order.DESCENDING to ThemeText(fileListOrderDescendingActionTitleKey),
)

private val filterTitles = mapOf(
    PathItemFilters.Empty to "",
    PathItemFilters.OnlyFile to ThemeText(fileListFilterOnlyFilesActionTitleKey),
    PathItemFilters.OnlyFolder to ThemeText(fileListFilterOnlyFoldersActionTitleKey),
)

fun StorageListScreen.DataResolver.parseSorterToAction(): Action {
    return Action(
        title = sorterTitles[sorter] ?: "",
        icon = EmptyDrawable
    )
}

fun StorageListScreen.DataResolver.parseOrderToAction(): Action {
    return Action(
        title = orderTitles[order] ?: "",
        icon = EmptyDrawable
    )
}

fun StorageListScreen.DataResolver.parseFilterToAction(): Action {
    return Action(
        title = filterTitles[filter] ?: "",
        icon = EmptyDrawable
    )
}