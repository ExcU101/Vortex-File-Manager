package io.github.excu101.vortex.ui.component

import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.manager.ui.theme.ThemeColorChangeListener
import io.github.excu101.manager.ui.theme.ThemeDimen
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

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

context (View)
fun Int.toDp(): Int {
    return dp
}

context (ViewGroup)
fun ThemeColorChangeListener.themeMeasure(
    widthSpec: Int,
    heightSpec: Int,
    widthKey: String? = null,
    heightKey: String? = null,
): Pair<Int, Int> {
    val widthSize = MeasureSpec.getSize(widthSpec)
    val widthMode = MeasureSpec.getMode(widthSpec)
    val heightSize = MeasureSpec.getSize(heightSpec)
    val heightMode = MeasureSpec.getMode(heightSpec)

    val desireWidth = widthKey?.let { ThemeUDp(it) } ?: widthSize
    val desireHeight = heightKey?.let { ThemeUDp(it) } ?: heightSize

    val width = if (desireWidth == MATCH_PARENT) widthSize else when (widthMode) {
        MeasureSpec.EXACTLY -> widthSize
        MeasureSpec.AT_MOST -> min(desireWidth, widthSize)
        else -> desireWidth
    } + paddingRight + paddingLeft

    val height = if (desireHeight == MATCH_PARENT) heightSize else when (heightMode) {
        MeasureSpec.EXACTLY -> heightSize
        MeasureSpec.AT_MOST -> min(desireHeight, heightSize)
        else -> desireHeight
    } + paddingTop + paddingBottom

    return width to height
}

var ViewPager2.sensitivityFactor: Int
    get() = 0
    set(value) {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * value)
    }

context(View)
fun ThemeDp(key: String): Int {
    return ThemeDimen(key).dp
}

context(View)
fun ThemeUDp(key: String): Int {
    return ThemeDimen(key).udp
}

context (View)
inline fun ThemeMaterialShapeDrawable(
    builder: ShapeAppearanceModel.Builder.() -> Unit,
    colorKey: String,
) = MaterialShapeDrawable(
    ShapeAppearanceModel.Builder().apply(builder).build()
).apply {
    setTint(ThemeColor(colorKey))
}

context(View)
val Int.dp: Int
    get() = ceil(context.resources.displayMetrics.density * this).roundToInt()

context (View)
val Int.udp: Int
    get() = if (this == -1) -1 else dp

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
