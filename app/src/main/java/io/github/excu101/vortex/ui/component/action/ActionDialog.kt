package io.github.excu101.vortex.ui.component.action

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerSurfaceColorKey

class ActionDialog(context: Context) : BottomSheetDialog(context) {

    private val adapter = ActionAdapter()
    private val background = MaterialShapeDrawable(
        builder()
            .setTopRightCorner(ROUNDED, 16F)
            .setTopLeftCorner(ROUNDED, 16F)
            .build()
    ).apply {
        fillColor = ColorStateList.valueOf(ThemeColor(mainDrawerSurfaceColorKey))
        initializeElevationOverlay(context)
    }

    var actions: List<ActionHeaderItem>
        get() = adapter.currentList
        set(value) {
            adapter.submitList(value)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root.addView(viewActions)
        setContentView(root)
    }

}