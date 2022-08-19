package io.github.excu101.vortex.ui.view.bar

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

class BottomBarBehavior : CoordinatorLayout.Behavior<BottomBar>() {

    var isVisible: Boolean = false

    internal fun slideDown(view: View) {
        isVisible = true
        view.translationY = 0F
    }

    internal fun slideUp(view: View) {
        isVisible = false
        view.translationY = -view.height.toFloat()
    }

}