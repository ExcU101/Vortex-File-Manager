package io.github.excu101.vortex.ui.component.drawer

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.view.updatePadding
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView

class BottomSheetDrawerBinding(context: Context) : ViewBinding<FrameLayout> {

    override val root: FrameLayout = FrameLayout(context)

    var items = ItemRecyclerView(root.context)

    override fun onCreate() {
        root.updatePadding(
            top = with(root) { 16.dp }
        )
        root.addView(items, MATCH_PARENT, WRAP_CONTENT)
    }

    override fun onDestroy() {

    }
}