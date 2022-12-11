package io.github.excu101.vortex.ui.component.list.adapter.dsl

import com.google.android.material.divider.MaterialDivider
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItemView
import io.github.excu101.vortex.ui.component.item.text.TextItemView
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory

// Just playing

interface ItemAdapterScope<T> {

    fun withViewType(viewType: Int, factory: ViewHolderFactory<T>)

}

inline fun <T : Item<*>> itemAdapter(block: ItemAdapterScope<T>.() -> Unit): ItemAdapter<T> {
    return ItemAdapter()
}

fun main() {
    val adapter = itemAdapter {
        withHolder(
            viewType = 4,
            view = { TextItemView(it.context) }
        ) {
            bind {

            }
        }
        withHolder(
            viewType = 3,
            view = { MaterialDivider(it.context) }
        ) {
            bind { value ->

            }
        }
        withHolder(
            viewType = 2,
            view = { DrawerItemView(it.context) }
        ) {
            with(view) {
                bind { value ->
                    icon = (value as DrawerItem).value.icon
                }

                listener<ViewListenerScope> {
                    onClick = { listener ->

                    }
                    onLongClick = { listener ->
                        false
                    }
                }
            }

        }
    }
}