package io.github.excu101.vortex.ui.screen.storage.pager.page.bookmark

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams.MATCH_PARENT
import io.github.excu101.vortex.ui.component.ItemViewTypes.drawerItem
import io.github.excu101.vortex.ui.component.ItemViewTypes.textItem
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory

class StorageBookmarkBinding(
    context: Context,
) : ViewBinding {

    override val root: CoordinatorLayout = CoordinatorLayout(context)

    val list = ItemRecyclerView(
        context,
        textItem to TextItem as ViewHolderFactory<Item<*>>,
        drawerItem to DrawerItem as ViewHolderFactory<Item<*>>
    )

    override fun onCreate() {
        root.addView(list, CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }

    override fun onDestroy() {

    }
}