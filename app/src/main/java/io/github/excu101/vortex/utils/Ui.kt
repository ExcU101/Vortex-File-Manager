package io.github.excu101.vortex.utils

import android.animation.ValueAnimator
import io.github.excu101.vortex.ui.component.ItemViewTypes

val ItemViewTypes.StorageItem: Int
    get() = 50

val ItemViewTypes.TrailItem: Int
    get() = 51

val ItemViewTypes.SwitchItem: Int
    get() = 52

fun BoolAnimator(
    prediction: Boolean,
): ValueAnimator {
    val start: Float
    val end: Float

    if (prediction) {
        start = 0F
        end = 1F
    } else {
        start = 1F
        end = 0F
    }

    return ValueAnimator.ofFloat(
        start, end
    )
}