package io.github.excu101.vortex.ui.component.item.menu

import android.content.Context
import android.widget.TextView
import androidx.core.view.contains
import io.github.excu101.vortex.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.menu.MenuLayout
import kotlin.math.min

class MenuHeader(context: Context) : io.github.excu101.vortex.theme.widget.ThemeFrameLayout(context) {

    private val desireHeight = 48.dp

    private val titleView = TextView(context).apply {

    }

    private val menuLayout = MenuLayout(context).apply {

    }

    private val isContainsTitle: Boolean
        get() = contains(titleView)

    private val isContainsMenu: Boolean
        get() = contains(menuLayout)

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

        var availableWidth = width - 16.dp

    }

    override fun onColorChanged() {

    }

}