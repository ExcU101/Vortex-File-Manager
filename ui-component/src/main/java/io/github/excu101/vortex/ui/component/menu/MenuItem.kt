package io.github.excu101.vortex.ui.component.menu

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.RippleDrawable
import android.view.View.MeasureSpec.*
import android.widget.ImageView
import android.widget.TextView
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.action
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.mainBarActionIconTintColorKey
import kotlin.math.min

class MenuItem(context: Context) : ThemeFrameLayout(context) {

    enum class Mode {
        TITLE,
        ICON
    }

    private val iconSize = 24.dp

    var mode = Mode.ICON
        set(value) {
            field = value
            invalidate()
        }

    var action: Action = action(title = "", icon = null)
        set(value) {
            field = value
            titleView.text = value.title
            iconView.setImageDrawable(value.icon)
        }

    private val titleView = TextView(context).apply {
        textSize = 16F
        setTextColor(ThemeColor(mainBarActionIconTintColorKey))
    }

    private val background =
        RippleDrawable(valueOf(ThemeColor(mainBarActionIconTintColorKey)), null, null)

    private val iconView = ImageView(context).apply {
        isClickable = true
        isFocusable = true
        background = this@MenuItem.background
        minimumWidth = 24.dp
        minimumHeight = 24.dp
        setColorFilter(ThemeColor(mainBarActionIconTintColorKey))
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (mode == Mode.ICON) {
            iconView.setOnClickListener(l)
        }
    }

    var titleColor: Int
        get() = titleView.currentTextColor
        set(value) {
            titleView.setTextColor(value)
        }

    var iconColor: Int
        get() {
            return iconView.imageTintList?.defaultColor ?: 0
        }
        set(value) {
            iconView.setColorFilter(value)
        }

    init {
        isClickable = true
        isFocusable = true
        if (mode == Mode.ICON) {
            addView(iconView)
        } else {
            addView(titleView)
        }
    }

    override fun onChanged() {
        iconView.setColorFilter(ThemeColor(mainBarActionIconTintColorKey))
        background.setColor(valueOf(ThemeColor(mainBarActionIconTintColorKey)))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec)
        val widthMode = getMode(widthMeasureSpec)
        val heightSize = getSize(heightMeasureSpec)
        val heightMode = getMode(heightMeasureSpec)

        val width = when (widthMode) {
            EXACTLY -> widthSize
            AT_MOST -> min(24.dp, widthSize)
            else -> 24.dp
        }


        val height = when (heightMode) {
            EXACTLY -> heightSize
            AT_MOST -> min(24.dp, heightSize)
            else -> 24.dp
        }

        setMeasuredDimension(width, height)

//        if (mode == Mode.ICON) {

        iconView.measure(
            makeMeasureSpec(24.dp, EXACTLY),
            makeMeasureSpec(24.dp, EXACTLY)
        )
//        } else {
//            titleView.measure(widthMeasureSpec, heightMeasureSpec)
//            setMeasuredDimension(titleView.measuredWidth, 56.dp)
//        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (mode == Mode.ICON) {
            iconView.layout(
                0,
                measuredHeight / 2,
                measuredWidth,
                measuredHeight / 2 + iconView.measuredHeight
            )
        }
    }


}