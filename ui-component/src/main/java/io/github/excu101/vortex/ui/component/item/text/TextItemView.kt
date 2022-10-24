package io.github.excu101.vortex.ui.component.item.text

import android.content.Context
import android.content.res.ColorStateList
import android.view.View.MeasureSpec.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.core.view.contains
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerTitleColorKey
import kotlin.math.min

class TextItemView(
    context: Context,
) : ThemeFrameLayout(context), ViewHolder.RecyclableView<TextItem> {

    private val innerWidthPadding = 16.dp

    private val desireWidth = MATCH_PARENT
    private val desireHeight = 48.dp

    private val title = TextView(context).apply {
        setTextColor(ThemeColor(mainDrawerTitleColorKey))
        textSize = 14F
    }

    private val shape = MaterialShapeDrawable().apply {
        initializeElevationOverlay(context)
        setTint(ThemeColor(mainDrawerBackgroundColorKey))
    }

    override fun setBackgroundTintList(tint: ColorStateList?) {
        shape.tintList = tint
    }

    private val containsTitle: Boolean
        get() = contains(title)

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(title)
        }
    }

    fun setTitleAlignment(alignment: Int) {
        title.textAlignment = alignment
    }

    init {
        background = shape
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
                (height - title.lineHeight) / 2 + title.lineHeight
            )
            layoutWidth += title.measuredWidth - innerWidthPadding
        }
    }

    override fun setBackgroundColor(color: Int) {
        shape.setTint(color)
    }

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)

        MaterialShapeUtils.setElevation(this, elevation)
    }

    override fun onChanged() {
        title.setTextColor(ThemeColor(mainDrawerTitleColorKey))
        setBackgroundColor(ThemeColor(mainDrawerBackgroundColorKey))
    }

    override fun onBind(item: TextItem) {
        setTitle(item.value)
        setTitleAlignment(item.alignment)
    }

    override fun onUnbind() {
        setTitle(null)
    }

}