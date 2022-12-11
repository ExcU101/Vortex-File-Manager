package io.github.excu101.vortex.ui.component.bar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView

private const val STATE_SCROLLED_DOWN = 1
private const val STATE_SCROLLED_UP = 2

class BarBehavior : CoordinatorLayout.Behavior<Bar> {

    constructor() : super()

    private var currentAnimator: ViewPropertyAnimator? = null
    private var state = 0

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Bar,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int,
    ): Boolean {
        return child.hideOnScroll && axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Bar,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray,
    ) {
        if (target !is ItemRecyclerView) {
            if (dyConsumed > 0) {
                slideDown(child)
            } else if (dyConsumed < 0) {
                slideUp(child)
            }
        }
    }

    internal fun slideUp(child: Bar, animate: Boolean = true) {
        if (state == STATE_SCROLLED_UP) return
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }
        state = STATE_SCROLLED_UP
        val targetTranslationY = 0
        if (animate) {
            child.animate().setDuration(100L).translationY(targetTranslationY.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimator = null
                    }
                })
        } else {
            child.translationY = targetTranslationY.toFloat()
        }
    }

    internal fun slideDown(child: Bar, animate: Boolean = true) {
        if (state == STATE_SCROLLED_DOWN) return
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }

        val targetTranslationY = child.height
        state = STATE_SCROLLED_DOWN
        if (animate) {
            child.animate().setDuration(100L).translationY(targetTranslationY.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimator = null
                    }
                })
        } else {
            child.translationY = targetTranslationY.toFloat()
        }
    }


}