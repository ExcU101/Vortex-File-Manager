package io.github.excu101.vortex.ui.component.drawer

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView
import io.github.excu101.vortex.ui.component.item.divider.DividerHeaderItem
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.with

class TaskBottomDrawer(
    context: Context,
) : BottomSheetDialog(
    context
) {
    private val adapter = ItemAdapter(
        ItemViewTypes.DrawerItem with DrawerItem,
        ItemViewTypes.DividerItem with DividerHeaderItem,
        ItemViewTypes.TextItem with TextItem
    )

    private val root = FrameLayout(context)
    private val recycler = ItemRecyclerView(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recycler.adapter = adapter

        root.addView(recycler, MATCH_PARENT, MATCH_PARENT)
        setContentView(root)
    }

}