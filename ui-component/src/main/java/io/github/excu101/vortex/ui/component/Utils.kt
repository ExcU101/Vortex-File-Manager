package io.github.excu101.vortex.ui.component

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import io.github.excu101.vortex.theme.ThemeColorChangeListener
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

context (View)
fun Int.toDp(): Int {
    return dp
}

context (View)
inline fun ThemeColorChangeListener.themeMeasure(
    widthSpec: Int,
    heightSpec: Int,
    widthKey: String? = null,
    heightKey: String? = null,
    onResult: (width: Int, height: Int) -> Unit,
) {
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

    onResult(width, height)
}

context(View)
fun ThemeDp(key: String): Int {
    return io.github.excu101.vortex.theme.ThemeDimen(key).dp
}

context(View)
fun ThemeUDp(key: String): Int {
    return io.github.excu101.vortex.theme.ThemeDimen(key).udp
}

context (View)
        inline fun ThemeMaterialShapeDrawable(
    builder: ShapeAppearanceModel.Builder.() -> Unit,
    colorKey: String,
) = MaterialShapeDrawable(
    ShapeAppearanceModel.Builder().apply(builder).build()
).apply {
    setTint(io.github.excu101.vortex.theme.ThemeColor(colorKey))
}

fun Context.dp(size: Float): Float = resources.displayMetrics.density * size

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
val Int.ddp: Int
    get() = dp

context(Drawable)
val Float.dp: Float
    get() = ceil(Resources.getSystem().displayMetrics.density * this)
