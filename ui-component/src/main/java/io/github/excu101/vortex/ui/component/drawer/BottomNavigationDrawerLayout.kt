package io.github.excu101.vortex.ui.component.drawer

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.Px
import androidx.core.view.updatePadding
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView
import io.github.excu101.vortex.ui.component.item.divider.DividerHeaderItem
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.icon.ActionItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory

class BottomNavigationDrawerLayout(
    context: Context,
) : BottomDrawerLayout(
    context
), NavigationDrawer {

    private val navigation = ItemRecyclerView(
        context,
        ItemViewTypes.TextItem to (TextItem as ViewHolderFactory<Item<*>>),
        ItemViewTypes.IconTextItem to (ActionItem as ViewHolderFactory<Item<*>>),
        ItemViewTypes.DrawerItem to (DrawerItem as ViewHolderFactory<Item<*>>),
        ItemViewTypes.DividerItem to (DividerHeaderItem as ViewHolderFactory<Item<*>>),
    ).apply {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    val adapter: ItemAdapter<Item<*>>
        get() = navigation.adapter

    init {
        replaceContainerView(navigation)
    }

    fun updateNavigationPaddings(
        @Px left: Int = 0,
        @Px top: Int = 0,
        @Px right: Int = 0,
        @Px bottom: Int = 0,
    ) {
        navigation.updatePadding(
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
    }

    override fun registerListener(listener: DrawerActionListener) {
        adapter.register { _, item, _ ->
            if (item is DrawerItem) {
                listener.onDrawerActionCall(item.value)
            }
        }
    }

    override fun replaceNavigation(items: List<Item<*>>) {
        adapter.replace(items, null)
    }

    override fun replaceSelected(items: List<Item<*>>) {
        adapter.replaceSelected(items)
    }

}