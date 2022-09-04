package io.github.excu101.vortex.ui.component.animation

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.fade(isOut: Boolean, isGoneOnOutEnd: Boolean = true) {
    if (isOut) {
        fadeOut(isGoneOnEnd = isGoneOnOutEnd)
    } else {
        fadeIn()
    }
}

fun View.fadeOut(isGoneOnEnd: Boolean = true) {
    if (animation != null) {
        clearAnimation()
    }

    animate().alphaBy(1F).alpha(0F).setDuration(500L).start()

    if (isGoneOnEnd) {
        isGone = isGoneOnEnd
    } else {
        isInvisible = true
    }
}

fun View.fadeIn() {
    if (animation != null) {
        clearAnimation()
    }
    if (!isVisible) {
        isVisible = true
    }

    animate().alphaBy(0F).alpha(1F).setDuration(500L).start()
}