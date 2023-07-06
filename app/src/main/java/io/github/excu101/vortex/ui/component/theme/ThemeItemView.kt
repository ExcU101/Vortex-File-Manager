package io.github.excu101.vortex.ui.component.theme

import android.content.Context
import android.widget.TextView
import io.github.excu101.vortex.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.dp
import kotlin.math.min

open class ThemeItemView(context: Context) : io.github.excu101.vortex.theme.widget.ThemeFrameLayout(context = context) {

    private val desireHeight = 48.dp

    private val nameView = TextView(context).apply {

    }

    override fun onColorChanged() {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec) // requires full width-size
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> widthSize
            else -> widthSize
        } + paddingRight + paddingLeft
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desireHeight, heightSize)
            else -> desireHeight
        } + paddingTop + paddingBottom

        setMeasuredDimension(width, height)
    }
}