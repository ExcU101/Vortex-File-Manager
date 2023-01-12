package io.github.excu101.vortex.ui.screen.storage.page.list

import android.graphics.drawable.Drawable
import android.view.View.OnClickListener
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemParsers
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.provider.storage.Filter
import io.github.excu101.vortex.provider.storage.Order
import io.github.excu101.vortex.provider.storage.ResultParser
import io.github.excu101.vortex.provider.storage.Sorter
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.screen.storage.Actions

object StorageListPageScreen {

    data class State(
        val data: List<Item<*>> = listOf(),
        val isLoading: Boolean = false,
        val loadingTitle: String? = null,
        val isWarning: Boolean = false,
        val warningIcon: Drawable? = null,
        val warningMessage: String? = null,
        val actions: List<MenuAction> = Actions.BarActions
    )

    sealed class SideEffect {
        data class Snackbar(
            val message: String,
            val messageDuration: Int = com.google.android.material.snackbar.Snackbar.LENGTH_SHORT,
            val messageActionTitle: String? = null,
            val messageAction: (OnClickListener)? = null,
        ) : SideEffect()

        class StorageItemCreate(
            val parent: PathItem? = null,
        ) : SideEffect()

        class StorageAction(
            val content: List<Item<*>>,
        ) : SideEffect()

        data class StorageFilter(
            val currentSorter: Sorter<PathItem>,
            val currentFilter: Filter<PathItem>
        ) : SideEffect()
    }

    data class DataResolver(
        val order: Order = Order.ASCENDING,
        val sorter: Sorter<PathItem> = PathItemSorters.Name,
        val filter: Filter<PathItem> = PathItemFilters.Empty,
        val parser: ResultParser<PathItem> = PathItemParsers.Default,
    ) {
        fun run(
            data: List<PathItem>,
        ): List<Item<*>> {
            val sorted = when (order) {
                Order.ASCENDING -> {
                    data.sortedWith(sorter)
                }

                Order.DESCENDING -> {
                    data.sortedWith(sorter).reversed()
                }
            }

            val filtered = sorted.filter(filter::accept)

            return parser.parse(filtered)
        }
    }

}

