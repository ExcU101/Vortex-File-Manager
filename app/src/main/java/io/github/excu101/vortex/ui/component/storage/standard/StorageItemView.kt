package io.github.excu101.vortex.ui.component.storage.standard

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.View.MeasureSpec.*
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.contains
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel.Builder
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeDimen
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.ThemeDp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.*
import kotlin.math.min

class StorageItemView(context: Context) : ThemeFrameLayout(context) {

    private val largeInnerPadding = 16.dp
    private val middleInnerPadding = 8.dp

    private val titleLeftPadding = ThemeDimen(fileItemTitleLeftPaddingKey).dp
    private val infoLeftPadding = ThemeDimen(fileItemInfoLeftPaddingKey).dp

    private val iconSize = 40.dp

    private val shapeInAnimator: ValueAnimator? = ValueAnimator.ofFloat(0F, 500F).apply {
        startDelay = 250L
        duration = 250L
        interpolator = AccelerateInterpolator()
        addUpdateListener {
            shape.setCornerSize(it.animatedValue as Float)
        }
    }

    private val shapeOutAnimator: ValueAnimator? = ValueAnimator.ofFloat(500F, 0F).apply {
        startDelay = 250L
        duration = 250L
        interpolator = AccelerateInterpolator()
        addUpdateListener {
            shape.setCornerSize(it.animatedValue as Float)
        }
    }

    var titleColor: Int
        get() = title.currentTextColor
        set(value) {
            title.setTextColor(value)
        }

    var infoColor: Int
        get() = info.currentTextColor
        set(value) {
            info.setTextColor(value)
        }

    var backgroundElevation: Float
        get() = shape.elevation
        set(value) {
            shape.elevation = value
        }

    val containsTitle: Boolean
        get() = contains(title)

    val containsInfo: Boolean
        get() = contains(info)

    val containsIcon: Boolean
        get() = contains(icon)

    private var isItemSelected = false

    private val iconShape = MaterialShapeDrawable(
        Builder().setAllCorners(CornerFamily.ROUNDED, 500F).build()
    ).apply {
        setTint(ThemeColor(fileItemIconBackgroundColorKey))
    }

    private val iconBackground = RippleDrawable(
        valueOf(ThemeColor(fileItemIconTintColorKey)),
        iconShape,
        null
    )

    private val shape = MaterialShapeDrawable(

    ).apply {
        initializeElevationOverlay(context)
        setTint(ThemeColor(fileItemSurfaceColorKey))
    }

    private val background = RippleDrawable(
        valueOf(ThemeColor(fileItemSurfaceRippleColorKey)),
        shape,
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
        setColorFilter(ThemeColor(fileItemIconTintColorKey))
    }

    private val title = TextView(context).apply {
        textSize = 16F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(ThemeColor(fileItemTitleTextColorKey))
    }

    private val info = TextView(context).apply {
        textSize = 14F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(ThemeColor(fileItemSecondaryTextColorKey))
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

    private fun ensureContainingInfo() {
        if (!containsInfo) {
            addView(info)
        }
    }

    fun setInfo(value: String? = null) {
        ensureContainingInfo()
        info.text = value
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

    fun clearItems() {
        title.text = null
        info.text = null
        icon.setImageDrawable(null)

        removeAllViews()
    }

    fun updateSelection(isSelected: Boolean) {
        isItemSelected = isSelected
        updateSelectionState()
    }

    private fun updateSelectionState() {
        if (isItemSelected) {
            titleColor = ThemeColor(fileItemTitleSelectedTextColorKey)
            infoColor = ThemeColor(fileItemSecondarySelectedTextColorKey)
            icon.setColorFilter(ThemeColor(fileItemIconSelectedTintColorKey))
            iconShape.setTint(ThemeColor(fileItemIconBackgroundSelectedColorKey))
            shape.setTint(ThemeColor(fileItemSurfaceSelectedColorKey))

//            shape.setCornerSize(500F)
        } else {
            titleColor = ThemeColor(fileItemTitleTextColorKey)
            infoColor = ThemeColor(fileItemSecondaryTextColorKey)
            icon.setColorFilter(ThemeColor(fileItemIconTintColorKey))
            iconShape.setTint(ThemeColor(fileItemIconBackgroundColorKey))
            shape.setTint(ThemeColor(fileItemSurfaceColorKey))

//            shape.setCornerSize(0F)
        }
    }

    override fun onChanged() {
        updateSelectionState()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }

    init {
        isClickable = true
        isFocusable = true
        minimumHeight = ThemeDp(fileItemHeightKey)
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
        if (containsInfo) {
            info.measure(makeMeasureSpec(availableWidth, AT_MOST), makeMeasureSpec(20.dp, AT_MOST))
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
                middleInnerPadding + title.lineHeight
            )
        }
        if (containsInfo) {
            info.layout(
                largeInnerPadding + infoLeftPadding + icon.measuredWidth,
                middleInnerPadding + title.lineHeight,
                largeInnerPadding + infoLeftPadding + icon.measuredWidth + info.measuredWidth,
                middleInnerPadding + title.lineHeight + info.lineHeight
            )
        }
    }

}