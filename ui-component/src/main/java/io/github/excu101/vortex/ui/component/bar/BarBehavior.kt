package io.github.excu101.vortex.ui.component.bar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewPropertyAnimator

private const val STATE_SCROLLED_DOWN = 1
private const val STATE_SCROLLED_UP = 2

class BarBehavior  {

    constructor() : super()

    private var currentAnimator: ViewPropertyAnimator? = null
    private var state = 0

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