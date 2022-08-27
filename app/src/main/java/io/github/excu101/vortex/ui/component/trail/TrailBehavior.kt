package io.github.excu101.vortex.ui.component.trail

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout

class TrailBehavior : CoordinatorLayout.Behavior<TrailView> {

    constructor() : super()

    private var currentAnimator: ViewPropertyAnimator? = null

    constructor(
        context: Context?,
        attrs: AttributeSet?,
    ) : super(
        context,
        attrs
    )

    var isVisible: Boolean = false

    fun slideDown(view: View) {
        if (currentAnimator != null) {
            currentAnimator?.cancel()
            view.clearAnimation()
        }
        isVisible = true
        currentAnimator = view.animate().translationY(0F)
    }

    fun slideUp(view: View) {
        if (currentAnimator != null) {
            currentAnimator?.cancel()
            view.clearAnimation()
        }
        isVisible = false
        currentAnimator = view.animate().translationY(view.height.toFloat())
    }
}