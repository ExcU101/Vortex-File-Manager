package io.github.excu101.vortex.ui.view.action

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.Gravity
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.contains
import com.google.android.material.shape.MaterialShapeDrawable
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.mainBarSurfaceColorKey
import io.github.excu101.vortex.ui.theme.key.mainDrawerActionIconTintColorKey
import io.github.excu101.vortex.ui.theme.key.mainDrawerActionTitleTextColorKey
import io.github.excu101.vortex.ui.view.dp
import kotlin.math.min

class ActionView(context: Context) : FrameLayout(context) {

    init {
        isClickable = true
        isFocusable = true
        background = RippleDrawable(
            valueOf(0xFF6200EE.toInt()),
            MaterialShapeDrawable().apply {
                setTint(Theme<Int, Color>(mainBarSurfaceColorKey))
            },
            null
        )
    }

    private val largePadding = 16.dp
    private val middlePadding = 8.dp

    private val iconSize = 24.dp

    private val icon: ImageView = ImageView(context).apply {
        minimumHeight = iconSize
        minimumWidth = iconSize
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    private val title: TextView = TextView(context).apply {
        textSize = 16F
        maxLines = 1
        isSingleLine = true
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    init {
        setTitleColor(Theme<Int, Color>(mainDrawerActionTitleTextColorKey))
        setIconTint(Theme<Int, Color>(mainDrawerActionIconTintColorKey))
    }

    val containsTitle: Boolean
        get() = contains(title)

    val containsIcon: Boolean
        get() = contains(icon)

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(title)
        }
    }

    private fun ensureContainingIcon() {
        if (!containsIcon) {
            addView(icon)
        }
    }

    fun setTitle(value: String) {
        ensureContainingTitle()
        title.text = value
    }

    fun setTitleColor(@ColorInt color: Int) {
        title.setTextColor(color)
    }

    fun setIcon(drawable: Drawable?) {
        ensureContainingIcon()
        icon.setImageDrawable(drawable)
    }

    fun setIconTint(@ColorInt color: Int) {
        icon.setColorFilter(color)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec) // requires full width-size
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            AT_MOST -> widthSize
            else -> widthSize
        }
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            AT_MOST -> min(56.dp, heightSize)
            else -> 56.dp
        }

        setMeasuredDimension(width, height)
        val availableWidth = width - largePadding
        icon.measure(makeMeasureSpec(iconSize, AT_MOST), makeMeasureSpec(iconSize, AT_MOST))
        title.measure(makeMeasureSpec(availableWidth, AT_MOST), makeMeasureSpec(20.dp, AT_MOST))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        icon.layout(
            largePadding,
            middlePadding,
            largePadding + iconSize,
            height - middlePadding
        )

        title.layout(
            largePadding * 2 + iconSize + largePadding,
            title.lineHeight,
            largePadding * 2 + iconSize + largePadding + title.width,
            height - title.lineHeight
        )
    }

}