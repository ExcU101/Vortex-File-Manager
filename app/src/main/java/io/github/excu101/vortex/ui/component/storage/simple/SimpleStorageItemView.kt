package io.github.excu101.vortex.ui.component.storage.simple

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.View.MeasureSpec.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.contains
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel.Builder
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeDimen
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.*
import kotlin.math.min

class SimpleStorageItemView(context: Context) : FrameLayout(context) {
    companion object {
        const val TITLE_INDEX = 0
        const val ICON_INDEX = 2
    }

    private val largeInnerPadding = 16.dp
    private val middleInnerPadding = 8.dp

    private val titleLeftPadding = ThemeDimen(fileItemTitleLeftPaddingKey).dp
    private val iconSize = 40.dp

    var titleColor: Int
        get() = title.currentTextColor
        set(value) {
            title.setTextColor(value)
        }

    var backgroundElevation: Float
        get() = shape.elevation
        set(value) {
            shape.elevation = value
        }

    val containsTitle: Boolean
        get() = contains(title)

    val containsIcon: Boolean
        get() = contains(icon)

    private val shape = MaterialShapeDrawable(
    ).apply {
        initializeElevationOverlay(context)
        fillColor = valueOf(ThemeColor(storageListItemSurfaceColorKey))
    }

    private val background = RippleDrawable(
        valueOf(ThemeColor(storageListItemIconTintColorKey)),
        shape,
        null
    )

    private val icon = ImageView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        minimumWidth = iconSize
        minimumHeight = iconSize
        isClickable = true
        isFocusable = true
        imageTintList = valueOf(ThemeColor(storageListItemIconTintColorKey))
    }

    private val title = TextView(context).apply {
        textSize = 16F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(ThemeColor(storageListItemTitleTextColorKey))
    }

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(title, TITLE_INDEX)
        }
    }

    fun setTitle(value: String? = null) {
        ensureContainingTitle()
        title.text = value
    }

    private fun ensureContainingIcon() {
        if (!containsIcon) {
            addView(icon, ICON_INDEX)
        }
    }

    fun setIcon(drawable: Drawable? = null) {
        ensureContainingIcon()
        icon.setImageDrawable(drawable)
    }

    fun setIcon(@DrawableRes id: Int) {
        ensureContainingIcon()
        icon.setImageResource(id)
    }

    fun clearItems() {
        title.text = null
        icon.setImageDrawable(null)

        removeAllViews()
    }

    fun updateSelection(isSelected: Boolean) {
        if (isSelected) {
            titleColor = ThemeColor(storageListItemTitleSelectedTextColorKey)
            icon.setColorFilter(ThemeColor(storageListItemIconSelectedTintColorKey))
            shape.fillColor = valueOf(ThemeColor(storageListItemSurfaceSelectedColorKey))
        } else {
            titleColor = ThemeColor(storageListItemTitleTextColorKey)
            icon.setColorFilter(ThemeColor(storageListItemIconTintColorKey))
            shape.fillColor = valueOf(ThemeColor(storageListItemSurfaceColorKey))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }

    init {
        isClickable = true
        isFocusable = true
        minimumHeight = ThemeDimen(fileItemHeightKey).dp
        setBackground(background)
    }

    fun setOnIconClickListener(listener: OnClickListener?) {
        icon.setOnClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: OnLongClickListener?) {
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
            AT_MOST -> min(suggestedMinimumHeight, heightSize)
            else -> suggestedMinimumHeight
        }

        setMeasuredDimension(width, height)

        val availableWidth = width - largeInnerPadding
        if (containsIcon) {
            icon.measure(makeMeasureSpec(iconSize, AT_MOST), makeMeasureSpec(iconSize, AT_MOST))
        }
        if (containsTitle) {
            title.measure(makeMeasureSpec(availableWidth, AT_MOST), makeMeasureSpec(24.dp, AT_MOST))
        }
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
                largeInnerPadding + icon.measuredWidth + titleLeftPadding,
                middleInnerPadding,
                largeInnerPadding + icon.measuredWidth + titleLeftPadding + title.measuredWidth,
                height - (middleInnerPadding + title.lineHeight)
            )
        }
    }
}