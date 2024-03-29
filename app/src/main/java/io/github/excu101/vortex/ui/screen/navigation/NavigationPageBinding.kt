package io.github.excu101.vortex.ui.screen.navigation

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import io.github.excu101.vortex.ui.component.bar.Bar

class NavigationPageBinding(
    private val context: Context,
) {
    var root: CoordinatorLayout? = null
    var bar: Bar? = null

    fun onCreate() {
        root = CoordinatorLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        bar = Bar(context).apply {
            layoutParams = CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                gravity = Gravity.BOTTOM
            }
        }

        root?.addView(bar)
    }

    fun onDestroy() {
        root = null
        bar = null
    }


}