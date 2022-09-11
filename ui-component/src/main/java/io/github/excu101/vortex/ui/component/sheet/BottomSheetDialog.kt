package io.github.excu101.vortex.ui.component.sheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.navigationBars
import androidx.core.view.updatePadding
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder

open class BottomSheetDialog(context: Context) : Dialog(context) {

    protected val container: SheetContainerView = SheetContainerView(context)
    private val background: MaterialShapeDrawable = MaterialShapeDrawable(
        builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 16F)
            .setTopRightCorner(CornerFamily.ROUNDED, 16F)
            .build()
    )

    init {
        container.fitsSystemWindows = true
        container.background = background
        WindowCompat.setDecorFitsSystemWindows(requireWindow(), false)
        requireWindow().addFlags(FLAG_LAYOUT_IN_SCREEN or FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        ViewCompat.setOnApplyWindowInsetsListener(container) { view, insets ->
            container.updatePadding(
                bottom = insets.getInsets(navigationBars()).bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(container)

        WindowCompat.getInsetsController(requireWindow(), container)
        val attrs = requireWindow().attributes
        attrs.width = MATCH_PARENT
        attrs.dimAmount = 0F
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        attrs.height = MATCH_PARENT
        requireWindow().attributes = attrs
    }

    fun requireWindow(): Window {
        return window ?: throw IllegalArgumentException()
    }

}