package io.github.excu101.vortex.ui.screen.storage.page.bookmark

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams.MATCH_PARENT
import io.github.excu101.vortex.ui.component.ItemViewTypes.DrawerItem
import io.github.excu101.vortex.ui.component.ItemViewTypes.TextItem
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory

class StorageBookmarkBinding(
    context: Context,
) : ViewBinding<CoordinatorLayout> {

    override val root: CoordinatorLayout = CoordinatorLayout(context)

    val list = ItemRecyclerView(
        context,
        TextItem to TextItem as ViewHolderFactory<Item<*>>,
        DrawerItem to DrawerItem as ViewHolderFactory<Item<*>>
    )

    override fun onCreate() {
        root.addView(list, CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }

    override fun onDestroy() {

    }
}