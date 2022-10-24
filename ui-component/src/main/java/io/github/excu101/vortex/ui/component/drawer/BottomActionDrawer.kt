package io.github.excu101.vortex.ui.component.drawer

import android.content.Context
import android.os.Bundle
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView
import io.github.excu101.vortex.ui.component.item.divider.DividerHeaderItem
import io.github.excu101.vortex.ui.component.item.icon.IconTextHeaderItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.EditableAdapter
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.selection.SelectableAdapter
import io.github.excu101.vortex.ui.component.sheet.BottomSheetDialog
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey

class BottomActionDrawer(
    context: Context,
) : BottomSheetDialog(context) {

    private val root = ItemRecyclerView(context).apply {
        adapter.add(
            ItemViewTypes.textItem to (TextItem as ViewHolderFactory<Item<*>>),
            ItemViewTypes.iconTextItem to (IconTextHeaderItem as ViewHolderFactory<Item<*>>),
            ItemViewTypes.dividerItem to (DividerHeaderItem as ViewHolderFactory<Item<*>>),
        )
        setBackgroundColor(ThemeColor(mainDrawerBackgroundColorKey))
    }

    fun registerListener(listener: DrawerActionListener) {
        root.adapter.register { view, item, pos ->
            if (item is IconTextHeaderItem) {
                listener.onDrawerActionCall(item.value)
            }
        }
    }

    val controller: SelectableAdapter<Item<*>>
        get() = root.adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(root)
    }
}