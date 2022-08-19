package io.github.excu101.vortex.ui.view.sheet

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

open class BottomSheetDialog(context: Context) : Dialog(context) {

    protected val container: SheetContainerView = SheetContainerView(context)

    init {
        container.fitsSystemWindows = true
        WindowCompat.setDecorFitsSystemWindows(requireWindow(), false)
        requireWindow().addFlags(FLAG_LAYOUT_IN_SCREEN or FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        ViewCompat.setOnApplyWindowInsetsListener(container) { view, insets ->
            container.insets = insets.toWindowInsets()
            WindowInsetsCompat.CONSUMED
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(container)

        WindowCompat.getInsetsController(requireWindow(), container)
        val attrs = requireWindow().attributes
        attrs.width = ViewGroup.LayoutParams.MATCH_PARENT
        attrs.dimAmount = 0F
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        attrs.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    fun requireWindow(): Window {
        return window ?: throw IllegalArgumentException()
    }

}