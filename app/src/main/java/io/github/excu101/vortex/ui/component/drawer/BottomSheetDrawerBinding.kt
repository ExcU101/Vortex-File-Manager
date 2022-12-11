package io.github.excu101.vortex.ui.component.drawer

import android.content.Context
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView

class BottomSheetDrawerBinding(context: Context) : ViewBinding<ItemRecyclerView> {

    override val root: ItemRecyclerView = ItemRecyclerView(context)

    override fun onCreate() {

    }

    override fun onDestroy() {

    }
}