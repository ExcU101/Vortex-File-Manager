package io.github.excu101.vortex.ui.view.storage

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import android.view.View.MeasureSpec.*
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.Builder
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.*
import io.github.excu101.vortex.ui.view.AnimatableColor
import io.github.excu101.vortex.ui.view.dp
import kotlin.math.min


class StorageItemView(context: Context) : FrameLayout(context) {

    companion object {
        const val TITLE_INDEX = 0
        const val INFO_INDEX = 1
        const val ICON_INDEX = 2
    }

    private val largeInnerPadding = 16.dp
    private val middleInnerPadding = 8.dp
    private val desireHeight = 56.dp

    private val iconSize = 40.dp

    var titleColor: Int
        get() = title.currentTextColor
        set(value) {
            title.setTextColor(value)
        }

    var infoColor: Int
        get() = title.currentTextColor
        set(value) {
            title.setTextColor(value)
        }

    val containsTitle: Boolean
        get() = contains(title)

    val containsInfo: Boolean
        get() = contains(info)

    val containsIcon: Boolean
        get() = contains(icon)

    var isItemSelected: Boolean = false


    private val iconBackground = RippleDrawable(
        valueOf(Theme<Int, Color>(fileItemIconTintColorKey)),
        MaterialShapeDrawable(
            Builder()
                .setAllCorners(CornerFamily.ROUNDED, 500F)
                .build()
        ).apply {
            fillColor = valueOf(Theme<Int, Color>(fileItemIconBackgroundColorKey))
        },
        null
    )

    private val icon = ImageView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        background = iconBackground
        minimumWidth = iconSize
        minimumHeight = iconSize
        isClickable = true
        isFocusable = true
        imageTintList = valueOf(Theme<Int, Color>(fileItemIconTintColorKey))
    }

    private val title = TextView(context).apply {
        textSize = 16F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(Theme<Int, Color>(fileItemTitleTextColorKey))
    }

    private val info = TextView(context).apply {
        textSize = 14F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(Theme<Int, Color>(fileItemSecondaryTextColorKey))
    }

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(title, TITLE_INDEX)
        }
    }

    fun setTitle(value: String) {
        ensureContainingTitle()
        title.text = value
    }

    private fun ensureContainingInfo() {
        if (!containsInfo) {
            addView(info, INFO_INDEX)
        }
    }

    fun setInfo(value: String) {
        ensureContainingInfo()
        info.text = value
    }

    private fun ensureContainingIcon() {
        if (!containsIcon) {
            addView(icon, ICON_INDEX)
        }
    }

    fun setIcon(@DrawableRes id: Int) {
        ensureContainingIcon()
        icon.setImageResource(id)
    }

    fun clearItems() {
        title.text = null
        info.text = null
        icon.setImageDrawable(null)

        removeAllViews()
    }

    fun updateSelection(isSelected: Boolean) {
        if (isSelected) {
            titleColor = Theme<Int, Color>(fileItemTitleSelectedTextColorKey)
            infoColor = Theme<Int, Color>(fileItemSecondarySelectedTextColorKey)
            icon.setColorFilter(Theme<Int, Color>(fileItemIconSelectedTintColorKey))
        } else {
            titleColor = Theme<Int, Color>(fileItemTitleTextColorKey)
            infoColor = Theme<Int, Color>(fileItemSecondaryTextColorKey)
            icon.setColorFilter(Theme<Int, Color>(fileItemIconTintColorKey))
        }
    }

    init {
        ensureContainingTitle()
        ensureContainingInfo()
        val outValue = TypedValue()
        getContext().theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        isClickable = true
        isFocusable = true
        background = LayerDrawable(
            arrayOf(
                ColorDrawable(
                    Theme<Int, Color>(fileItemSurfaceColorKey)
                ),
                ContextCompat.getDrawable(context, outValue.resourceId),
            )
        )
    }

    fun setOnIconClickListener(listener: OnClickListener) {
        icon.setOnClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: OnLongClickListener) {
        icon.setOnLongClickListener(listener)
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
            AT_MOST -> min(desireHeight, heightSize)
            else -> desireHeight
        }

        setMeasuredDimension(width, height)

        val availableWidth = width - largeInnerPadding
        icon.measure(makeMeasureSpec(iconSize, AT_MOST), makeMeasureSpec(iconSize, AT_MOST))
        title.measure(makeMeasureSpec(availableWidth, AT_MOST), makeMeasureSpec(24.dp, AT_MOST))
        info.measure(makeMeasureSpec(availableWidth, AT_MOST), makeMeasureSpec(16.dp, AT_MOST))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (containsIcon) {
            icon.layout(
                largeInnerPadding,
                middleInnerPadding,
                largeInnerPadding + icon.measuredWidth,
                height - middleInnerPadding
            )
        }
        if (containsTitle) {
            title.layout(
                largeInnerPadding + icon.measuredWidth + largeInnerPadding,
                middleInnerPadding,
                largeInnerPadding + icon.measuredWidth + largeInnerPadding + title.measuredWidth,
                middleInnerPadding + title.lineHeight
            )
        }
        if (containsInfo) {
            info.layout(
                largeInnerPadding + icon.measuredWidth + largeInnerPadding,
                middleInnerPadding + title.lineHeight,
                largeInnerPadding + largeInnerPadding + icon.measuredWidth + info.measuredWidth,
                middleInnerPadding + title.lineHeight + info.lineHeight
            )
        }
    }

}