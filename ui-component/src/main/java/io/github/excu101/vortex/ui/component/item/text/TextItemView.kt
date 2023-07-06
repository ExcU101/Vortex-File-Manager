package io.github.excu101.vortex.ui.component.item.text

import android.content.Context
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.getMode
import android.view.View.MeasureSpec.getSize
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.core.view.contains
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.theme.key.mainDrawerTitleColorKey
import kotlin.math.min

class TextItemView(
    context: Context,
) : io.github.excu101.vortex.theme.widget.ThemeFrameLayout(context), ViewHolder.RecyclableView<TextItem> {

    private val innerWidthPadding = 16.dp

    private val desireWidth = MATCH_PARENT
    private val desireHeight = 48.dp

    private val title = TextView(context).apply {
        setTextColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainDrawerTitleColorKey))
        textSize = 14F
    }

    private val containsTitle: Boolean
        get() = contains(title)

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(title)
        }
    }

    fun setTitleAlignment(alignment: Int) {
        ensureContainingTitle()
        title.textAlignment = alignment
    }

    fun setTitleColor(color: Int) {
        ensureContainingTitle()
        title.setTextColor(color)
    }

    fun setTitleTextSize(size: Float) {
        ensureContainingTitle()
        title.textSize = size
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

        var availableWidth = width - innerWidthPadding
        if (containsTitle) {
            title.measure(makeMeasureSpec(availableWidth, EXACTLY), makeMeasureSpec(24.dp, EXACTLY))
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var layoutWidth = innerWidthPadding

        if (containsTitle) {
            title.layout(
                layoutWidth,
                (height - title.lineHeight) / 2,
                layoutWidth + title.measuredWidth - innerWidthPadding,
                (height - title.lineHeight) / 2 + title.lineHeight + 3.dp
            )
            layoutWidth += title.measuredWidth - innerWidthPadding
        }
    }

    override fun onColorChanged() {
        title.setTextColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainDrawerTitleColorKey))
    }

    override fun onBind(item: TextItem) {
        setTitle(item.value)
        setTitleAlignment(item.attrs.alignment)
        setTitleColor(item.attrs.color)
        setTitleTextSize(item.attrs.size)
    }

    override fun onUnbind() {
        setTitle(null)
    }

}