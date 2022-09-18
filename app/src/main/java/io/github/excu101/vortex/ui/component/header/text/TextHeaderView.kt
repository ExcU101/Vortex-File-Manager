package io.github.excu101.vortex.ui.component.header.text

import android.content.Context
import android.graphics.Color
import android.view.View.MeasureSpec.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.core.view.contains
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerTitleColorKey
import kotlin.math.min

class TextHeaderView(
    context: Context,
) : ThemeFrameLayout(context) {

    private val desireWidth = MATCH_PARENT
    private val desireHeight = 48.dp

    private val title = TextView(context).apply {
        setTextColor(ThemeColor(mainDrawerTitleColorKey))
        textSize = 14F
    }

    private val containsTitle: Boolean
        get() = contains(title)

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(title)
        }
    }

    init {
        setBackgroundColor(ThemeColor(mainDrawerBackgroundColorKey))
    }

    fun setTitle(value: String? = null) {
        ensureContainingTitle()
        title.text = value
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec)
        val widthMode = getMode(widthMeasureSpec)
        val heightSize = getSize(heightMeasureSpec)
        val heightMode = getMode(heightMeasureSpec)

        val width = when (widthMode) {
            EXACTLY -> widthSize
            AT_MOST -> widthSize
            else -> desireWidth
        } + paddingRight + paddingLeft

        val height = when (heightMode) {
            EXACTLY -> heightSize
            AT_MOST -> min(desireHeight, heightSize)
            else -> desireHeight
        } + paddingTop + paddingBottom

        setMeasuredDimension(width, height)

        var availableWidth = width
        title.measure(makeMeasureSpec(availableWidth, EXACTLY), makeMeasureSpec(24.dp, EXACTLY))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        title.layout(
            16.dp,
            (height - title.lineHeight) / 2,
            16.dp + title.measuredWidth,
            (height - title.lineHeight) / 2 + title.lineHeight
        )
    }

    override fun onChanged() {
        title.setTextColor(ThemeColor(mainDrawerTitleColorKey))
        setBackgroundColor(ThemeColor(mainDrawerBackgroundColorKey))
    }

}