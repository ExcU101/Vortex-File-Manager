package io.github.excu101.vortex.ui.component.sheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.widget.FrameLayout
import androidx.core.view.WindowCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearanceModel.*
import io.github.excu101.pluginsystem.model.Color

open class BottomSheetDialog(
    context: Context,
) : Dialog(context) {

    private val shape = MaterialShapeDrawable(
        builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 16F)
            .setTopRightCorner(CornerFamily.ROUNDED, 16F)
            .build()
    ).apply {

    }

    protected val container = FrameLayout(context).apply {
        background = shape
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(requireWindow()) {
            statusBarColor = Color.Transparent.value
            navigationBarColor = Color.Transparent.value

            addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            setLayout(MATCH_PARENT, MATCH_PARENT)
        }
        setContentView(container)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        container.fitsSystemWindows = true
        WindowCompat.setDecorFitsSystemWindows(requireWindow(), true)
    }

    protected fun requireWindow(): Window {
        return window ?: throw IllegalArgumentException()
    }
}