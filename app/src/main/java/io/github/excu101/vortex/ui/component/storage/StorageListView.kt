package io.github.excu101.vortex.ui.component.storage

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeColorChangeListener
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.item.icon.IconTextHeaderItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.theme.key.storageListListBackgroundColorKey
import io.github.excu101.vortex.utils.storageItem

class StorageListView(context: Context) : RecyclerView(context),
    CoordinatorLayout.AttachedBehavior, ThemeColorChangeListener {

    private val behavior: StorageListViewBehavior = StorageListViewBehavior()

    private val adapter = ItemAdapter(
        ItemViewTypes.textItem to (TextItem as ViewHolderFactory<Item<*>>),
        ItemViewTypes.iconTextItem to (IconTextHeaderItem as ViewHolderFactory<Item<*>>),
        ItemViewTypes.storageItem to (PathItem as ViewHolderFactory<Item<*>>)
    )

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(this.adapter)
    }

    override fun getAdapter() = adapter

    init {
        setHasFixedSize(true)
        setAdapter(adapter)
        isNestedScrollingEnabled = true
        clipToPadding = false
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        setBackgroundColor(ThemeColor(storageListListBackgroundColorKey))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Theme.registerColorChangeListener(listener = this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Theme.unregisterColorChangeListener(listener = this)
    }

    override fun onChanged() {
        setBackgroundColor(ThemeColor(storageListListBackgroundColorKey))
    }

    override fun getBehavior() = behavior

}