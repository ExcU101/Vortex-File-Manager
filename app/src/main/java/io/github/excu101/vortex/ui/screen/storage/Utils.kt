package io.github.excu101.vortex.ui.screen.storage

import android.content.Context
import android.graphics.drawable.Drawable
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.action
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.R
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.base.impl.Sorter
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen

private val sorterTitles = mapOf(
    PathItemSorters.Name to ThemeText(fileListSortNameActionTitleKey),
    PathItemSorters.Path to ThemeText(fileListSortPathActionTitleKey),
    PathItemSorters.Size to ThemeText(fileListSortSizeActionTitleKey),
    PathItemSorters.LastAccessTime to ThemeText(fileListSortLastAccessTimeActionTitleKey),
    PathItemSorters.LastModifiedTime to ThemeText(fileListSortLastModifiedTimeActionTitleKey),
    PathItemSorters.CreationTime to ThemeText(fileListSortCreationTimeActionTitleKey),
)

private val sorterIcons = mapOf<Sorter<PathItem>, Drawable>(

)

private val orderTitles = mapOf(
    Order.ASCENDING to ThemeText(fileListOrderAscendingActionTitleKey),
    Order.DESCENDING to ThemeText(fileListOrderDescendingActionTitleKey),
)

private val orderIcons = mapOf<Order, Drawable>(

)

private val filterTitles = mapOf(
    PathItemFilters.Empty to "",
    PathItemFilters.OnlyFile to ThemeText(fileListFilterOnlyFilesActionTitleKey),
    PathItemFilters.OnlyFolder to ThemeText(fileListFilterOnlyFoldersActionTitleKey),
)

private val filterIcons = mapOf(
    PathItemFilters.OnlyFile to R.drawable.ic_file_24,
    PathItemFilters.OnlyFolder to R.drawable.ic_folder_24,
)

fun StorageListPageScreen.DataResolver.parseSorterToAction(

): Action {
    return action(
        title = sorterTitles[sorter] ?: "",
        icon = sorterIcons[sorter]
    )
}

fun StorageListPageScreen.DataResolver.parseOrderToAction(

): Action {
    return action(
        title = orderTitles[order] ?: "",
        icon = orderIcons[order]
    )
}

fun StorageListPageScreen.DataResolver.parseFilterToAction(
    context: Context,
): Action {
    return action(
        title = filterTitles[filter] ?: "",
        icon = filterIcons[filter]?.let { context.getDrawable(it) }
    )
}