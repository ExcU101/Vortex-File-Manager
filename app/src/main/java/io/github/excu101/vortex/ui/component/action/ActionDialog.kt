package io.github.excu101.vortex.ui.component.action

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.menu.ActionListener
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerSurfaceColorKey

class ActionDialog(context: Context) : BottomSheetDialog(context) {

    private val listeners = mutableListOf<ActionListener>()
    private val adapter = ActionAdapter()
    private val background = MaterialShapeDrawable(
        builder()
            .setTopRightCorner(ROUNDED, 16F)
            .setTopLeftCorner(ROUNDED, 16F)
            .build()
    ).apply {
        initializeElevationOverlay(context)

        setTint(ThemeColor(mainDrawerSurfaceColorKey))
    }

    var actions: List<Item<*>>
        get() = adapter.list
        set(value) {
            adapter.replace(value)
        }

    private val root = CoordinatorLayout(context).apply {
        background = this@ActionDialog.background
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    private val viewActions: RecyclerView = RecyclerView(context).apply {
        layoutManager = LinearLayoutManager(context)
        adapter = this@ActionDialog.adapter
    }

    fun requireWindow(): Window {
        return window ?: throw IllegalArgumentException()
    }

    fun registerListener(listener: ActionListener) {
        listeners.add(listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root.addView(viewActions)
        setContentView(root)

        adapter.register { view, item, position ->
            listeners.forEach {
                it.onCall((item as ActionHeaderItem).value)
            }
        }

        listeners.add { action ->
            when (action.title) {
                "Switch theme" -> {
                    Theme.switch()
                }
            }
        }
    }

}