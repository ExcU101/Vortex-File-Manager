package io.github.excu101.vortex.ui.component.header.action

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.View.MeasureSpec.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.pluginsystem.model.Color
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.pluginsystem.utils.EmptyDrawable
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerItemBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerItemIconTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerItemTitleTextColorKey
import kotlin.math.min

class ActionHeaderView(context: Context) : ThemeFrameLayout(context) {

    private val desireHeight = 48.dp
    private val iconHorizontalPadding = 16.dp
    private val titleHorizontalPadding = 32.dp
    private val additionalTitleViewHeight = 2.dp
    private val iconSize = 24.dp

    private val shape = MaterialShapeDrawable().apply {
        initializeElevationOverlay(context)
        setTint(ThemeColor(mainDrawerItemBackgroundColorKey))
    }

    private val background = RippleDrawable(
        valueOf(Color.DarkGray.value),
        shape,
        null
    )

    private val iconView = ImageView(context).apply {
        setColorFilter(ThemeColor(mainDrawerItemIconTintColorKey))
    }

    private val titleView = TextView(context).apply {
        textSize = 16F
        setTextColor(ThemeColor(mainDrawerItemTitleTextColorKey))
    }

    var icon: Drawable?
        get() = iconView.drawable
        set(value) {
            iconView.setImageDrawable(value)
        }

    private val isEmptyIcon: Boolean
        get() = iconView.drawable == EmptyDrawable

    var title: CharSequence?
        get() = titleView.text
        set(value) {
            titleView.text = value
        }

    init {
        minimumWidth = MATCH_PARENT
        minimumHeight = desireHeight

        isClickable = true
        isFocusable = true

        addView(iconView)
        addView(titleView)

        setBackground(background)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec) // requires full width-size
        val widthMode = getMode(widthMeasureSpec)
        val heightSize = getSize(heightMeasureSpec)
        val heightMode = getMode(heightMeasureSpec)

        val width = when (widthMode) {
            EXACTLY -> widthSize
            AT_MOST -> widthSize
            else -> widthSize
        } + paddingRight + paddingLeft
        val height = when (heightMode) {
            EXACTLY -> heightSize
            AT_MOST -> min(desireHeight, heightSize)
            else -> desireHeight
        } + paddingTop + paddingBottom

        setMeasuredDimension(width, height)
        val availableWidth = width - iconHorizontalPadding

        if (!isEmptyIcon) {
            iconView.measure(
                makeMeasureSpec(iconSize, AT_MOST),
                makeMeasureSpec(iconSize, AT_MOST)
            )
        }
        titleView.measure(
            makeMeasureSpec(availableWidth, EXACTLY),
            makeMeasureSpec(iconSize, AT_MOST)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var widthLeft = iconHorizontalPadding
        if (!isEmptyIcon) {
            iconView.layout(
                widthLeft,
                height / 2 - iconView.measuredHeight,
                widthLeft + iconSize,
                height / 2 + iconView.measuredHeight
            )
            widthLeft += iconSize
        }

        if (!isEmptyIcon) {
            widthLeft += titleHorizontalPadding
        }
        titleView.layout(
            widthLeft,
            (height - titleView.lineHeight) / 2,
            widthLeft + titleView.measuredWidth,
            (height - titleView.lineHeight) / 2 + titleView.lineHeight + additionalTitleViewHeight
        )
    }

    override fun onChanged() {
        iconView.setColorFilter(ThemeColor(mainDrawerItemIconTintColorKey))
        shape.setTint(ThemeColor(mainDrawerItemBackgroundColorKey))
        titleView.setTextColor(ThemeColor(mainDrawerItemTitleTextColorKey))
    }

}