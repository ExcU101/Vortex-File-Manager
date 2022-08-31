package io.github.excu101.vortex.ui.component

import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import kotlin.math.ceil

@Suppress(names = ["FunctionName"])
inline fun AnimatableColor(
    @ColorInt from: Int,
    @ColorInt to: Int,
    crossinline onChange: (Int) -> Unit,
) {
    val _animator = ValueAnimator.ofArgb(from, to)
    _animator.addUpdateListener { animator ->
        onChange((animator.animatedValue as Int))
    }
    _animator.start()
}


class Position(val x: Int, val y: Int)

fun ViewGroup.removeViewFrom(range: IntRange) {
    for (i in range) {
        removeViewAt(i)
    }
}

context(View)
val Int.dp: Int
    get() = ceil(context.resources.displayMetrics.density * this).toInt()

context(View)
val Float.dp: Float
    get() = ceil(context.resources.displayMetrics.density * this)

context(View)
val Int.sp: Int
    get() = ceil(context.resources.displayMetrics.scaledDensity * this).toInt()

context(View)
val Float.sp: Float
    get() = ceil(context.resources.displayMetrics.scaledDensity * this)

context(Drawable)
val Int.dp: Int
    get() = ceil(Resources.getSystem().displayMetrics.density * this).toInt()

context(Drawable)
val Float.dp: Float
    get() = ceil(Resources.getSystem().displayMetrics.density * this)