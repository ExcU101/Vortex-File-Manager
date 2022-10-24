package io.github.excu101.vortex.ui.component.item.divider

import android.view.View
import android.view.ViewGroup
import com.google.android.material.divider.MaterialDivider
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

object DividerHeaderItem : Item<Unit>, ViewHolderFactory<DividerHeaderItem> {

    override val id: Long
        get() = Long.MIN_VALUE

    override val value: Unit
        get() = Unit

    override val type: Int
        get() = ItemViewTypes.dividerItem

    override fun produceView(parent: ViewGroup): View {
        return MaterialDivider(parent.context)
    }

}

fun ItemScope<Item<*>>.divider() = add(DividerHeaderItem)