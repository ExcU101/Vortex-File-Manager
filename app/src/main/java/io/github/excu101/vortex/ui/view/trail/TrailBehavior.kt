package io.github.excu101.vortex.ui.view.trail

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

class TrailBehavior : CoordinatorLayout.Behavior<TrailView> {

    constructor() : super()

    constructor(
        context: Context?,
        attrs: AttributeSet?,
    ) : super(
        context,
        attrs
    )

    var isVisible: Boolean = false

    fun slideDown(view: View) {
        isVisible = true
        view.translationY = 0F
    }

    fun slideUp(view: View) {
        isVisible = false
        view.translationY = -view.height.toFloat()
    }
}