package io.github.excu101.vortex.ui.component.list.adapter.dsl

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItemView
import io.github.excu101.vortex.ui.component.list.adapter.EditableAdapter
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter

@DslMarker
annotation class AdapterMarker

@AdapterMarker
interface AdapterScope {
    interface Extension
    interface BindingExtension : Extension {
        val position: Int
        val holder: ViewHolder
    }

    interface UnbindingExtension : Extension {
        val holder: ViewHolder
    }

    interface AttachExtension : Extension
    interface DetachExtension : Extension

    fun <E : Extension> addExtension(extension: E)

    fun addViewHolder(viewType: Int, provider: (parent: ViewGroup) -> View)
}

inline fun <T : Item<*>> adapter(scope: AdapterScope.() -> Unit): EditableAdapter<T> = ItemAdapter()

fun main() {
    adapter<Item<*>> {
        viewHolders(
            count = 2,
            provider = {
                View(context)
            },
            scopes = {
                scope {

                }
                scope {

                }
            },
        )
        viewHolder(
            type = 1,
            provider = { View(context) },
            scope = {
                diff<DrawerItem> {
                    areItemsSame = { old, new -> old.id == new.id }
                    areContentsSame = (DrawerItem::equals)
                }

                bind<DrawerItemView, DrawerItem> {
                    view.title = item.value.title
                    view.icon = item.value.icon
                }

                unbind<DrawerItemView> {
                    view.setOnClickListener(null)
                }

                listener<DrawerItemView> {
                    onClick = { listener ->

                    }
                    onLongClick = { listener ->

                    }
                }
            }
        )
    }
}