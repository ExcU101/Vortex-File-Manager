package io.github.excu101.vortex.ui.screen.storage.page.list

import android.graphics.drawable.Drawable
import android.view.View
import com.google.android.material.snackbar.Snackbar
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortex.base.impl.Filter
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.base.impl.ResultParser
import io.github.excu101.vortex.base.impl.Sorter
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemParsers
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.ui.component.list.adapter.Item

object StorageListPageScreen {

    data class State(
        val data: List<Item<*>> = listOf(),
        val isLoading: Boolean = false,
        val loadingTitle: String? = null,
        val isWarning: Boolean = false,
        val warningIcon: Drawable? = null,
        val warningMessage: String? = null,
        val warningActions: List<Action> = listOf(),
    )

    data class SideEffect(
        val message: String? = null,
        val messageDuration: Int = Snackbar.LENGTH_SHORT,
        val messageActionTitle: String? = null,
        val messageAction: (View.OnClickListener)? = null,
    )

    sealed class Dialog {
        class StorageItemCreate(
            val parent: PathItem? = null,
        ) : Dialog()

        class StorageAction(
            val content: List<Item<*>>,
        ) : Dialog()
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

