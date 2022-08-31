package io.github.excu101.vortex.ui.component.header.action

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View.MeasureSpec.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import io.github.excu101.vortex.ui.component.dp
import kotlin.math.min

class ActionHeaderView(context: Context) : FrameLayout(context) {

    private val desireHeight = 48.dp
    private val iconHorizontalPadding = 16.dp
    private val titleHorizontalPadding = 32.dp
    private val iconSize = 24.dp

    private val iconView = ImageView(context).apply {

    }

    private val titleView = TextView(context).apply {
        textSize = 14F
    }

    var icon: Drawable?
        get() = iconView.drawable
        set(value) {
            iconView.setImageDrawable(value)
        }

    var title: CharSequence?
        get() = titleView.text
        set(value) {
            titleView.text = value
        }

    init {
        minimumWidth = MATCH_PARENT
        minimumHeight = desireHeight

        addView(iconView)
        addView(titleView)
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
        }
        val height = when (heightMode) {
            EXACTLY -> heightSize
            AT_MOST -> min(suggestedMinimumHeight, heightSize)
            else -> suggestedMinimumHeight
        }

        setMeasuredDimension(width, height)
        val availableWidth = width - iconHorizontalPadding

        iconView.measure(makeMeasureSpec(iconSize, AT_MOST), makeMeasureSpec(iconSize, AT_MOST))
        titleView.measure(
            makeMeasureSpec(availableWidth, EXACTLY),
            makeMeasureSpec(iconSize, AT_MOST)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        iconView.layout(
            iconHorizontalPadding,
            4.dp,
            iconHorizontalPadding + iconSize,
            height - 4.dp
        )

        titleView.layout(
            iconHorizontalPadding + iconSize + titleHorizontalPadding,
            16.dp,
            iconHorizontalPadding + iconSize + titleHorizontalPadding + titleView.measuredWidth,
            16.dp + titleView.lineHeight
        )
    }

}