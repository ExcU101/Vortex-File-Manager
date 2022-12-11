package io.github.excu101.vortex.ui.component.animation

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun View.fade(
    isOut: Boolean,
    duration: Long = 500L,
    isGoneOnOutEnd: Boolean = true,
) {
    fade(
        isOut = isOut,
        durationIn = duration,
        durationOut = duration,
        isGoneOnOutEnd = isGoneOnOutEnd
    )
}

fun View.fade(
    isOut: Boolean,
    durationIn: Long = 500L,
    durationOut: Long = 500L,
    isGoneOnOutEnd: Boolean = true,
) {
    if (isOut) {
        fadeOut(
            duration = durationOut,
            isGoneOnEnd = isGoneOnOutEnd
        )
    } else {
        fadeIn(
            duration = durationIn
        )
    }
}

fun View.fadeOut(
    duration: Long = 500L, isGoneOnEnd: Boolean = true,
) {
    if (animation != null) {
        clearAnimation()
    }

    animate().alphaBy(1F).alpha(0F).setDuration(duration).start()

    if (isGoneOnEnd) {
        isGone = isGoneOnEnd
    } else {
        isInvisible = true
    }
}

fun View.fadeIn(
    duration: Long = 500L,
) {
    if (animation != null) {
        clearAnimation()
    }
    if (!isVisible) {
        isVisible = true
    }

    animate().alphaBy(0F).alpha(1F).setDuration(duration).start()
}