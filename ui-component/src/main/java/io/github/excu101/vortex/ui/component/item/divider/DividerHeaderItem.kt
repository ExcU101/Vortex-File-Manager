package io.github.excu101.vortex.ui.component.item.divider

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import com.google.android.material.divider.MaterialDivider
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.theme.key.mainDrawerDividerColorKey

object DividerHeaderItem : Item<Unit>, ViewHolderFactory<DividerHeaderItem> {

    override val id: Long
        get() = Long.MIN_VALUE

    override val value: Unit
        get() = Unit

    override val type: Int
        get() = ItemViewTypes.DividerItem

    override fun produceView(parent: ViewGroup): View {
        return MaterialDivider(parent.context).apply {
            dividerColor =
                io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainDrawerDividerColorKey)
            updatePadding(
                top = 3.dp,
                bottom = 3.dp
            )
        }
    }

}

fun ItemScope<Item<*>>.divider() = add(DividerHeaderItem)