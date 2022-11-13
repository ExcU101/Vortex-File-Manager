package io.github.excu101.vortex.ui.component.storage.simple.linear

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
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeDimen
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder.RecyclableView
import io.github.excu101.vortex.ui.component.theme.key.*
import kotlin.math.min

class SimpleStorageLinearCell(context: Context) : FrameLayout(context), RecyclableView<PathItem> {

    private val largeInnerPadding = 16.dp
    private val middleInnerPadding = 8.dp

    private val titleLeftPadding = ThemeDimen(storageListItemHorizontalTitlePaddingKey).dp
    private val iconSize = 40.dp

    var titleColor: Int
        get() = title.currentTextColor
        set(value) {
            title.setTextColor(value)
        }

    private val containsTitle: Boolean
        get() = contains(title)

    private val containsIcon: Boolean
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
            addView(title)
        }
    }

    fun setTitle(value: String? = null) {
        ensureContainingTitle()
        title.text = value
    }

    private fun ensureContainingIcon() {
        if (!containsIcon) {
            addView(icon)
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

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)

        shape.elevation = elevation
    }

    override fun getElevation(): Float = shape.elevation

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        MaterialShapeDrawable.createWithElevationOverlay(context, elevation)

        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }

    init {
        isClickable = true
        isFocusable = true
        minimumHeight = ThemeDimen(storageListItemLinearHeightDimenKey).dp
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
        var leftPosition = largeInnerPadding

        if (containsIcon) {
            icon.layout(
                leftPosition,
                middleInnerPadding,
                leftPosition + icon.measuredWidth,
                height - middleInnerPadding
            )
            leftPosition += icon.measuredHeight
        }
        if (containsTitle) {
            title.layout(
                leftPosition + titleLeftPadding,
                middleInnerPadding,
                leftPosition + titleLeftPadding + title.measuredWidth,
                height - (middleInnerPadding + title.lineHeight)
            )
            leftPosition += titleLeftPadding + title.measuredWidth
        }
    }

    override fun onBind(item: PathItem) {
        setTitle(item.name)
        setIcon(item.icon)
    }

    override fun onUnbind() {
        setTitle(null)
        setIcon(null)
    }
}