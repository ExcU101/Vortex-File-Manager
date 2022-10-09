package io.github.excu101.vortex.ui.component.drawer

import android.content.Context
import android.os.Bundle
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.header.icon.IconTextHeaderItem
import io.github.excu101.vortex.ui.component.menu.ActionListener
import io.github.excu101.vortex.ui.component.sheet.BottomSheetDialog
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey

class BottomActionDrawer(
    context: Context,
) : BottomSheetDialog(context) {

    private val adapter = ActionAdapter()
    private val root = RecyclerView(context).apply {
        setBackgroundColor(ThemeColor(mainDrawerBackgroundColorKey))
        adapter = this@BottomActionDrawer.adapter
        layoutManager = LinearLayoutManager(context)
    }

    fun registerListener(listener: ActionListener) {
        controller.register { view, item, pos ->
            if (item is IconTextHeaderItem) {
                listener.onCall(item.value)
            }
        }
    }

    val controller
        get() = adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        container.addView(root)
    }

    override fun onStart() {
        super.onStart()
    }

}