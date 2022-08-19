package io.github.excu101.vortex.ui.view.bar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.View.MeasureSpec.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.contains
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.mainBarSurfaceColorKey
import io.github.excu101.vortex.ui.view.dp
import io.github.excu101.vortex.ui.view.foundtation.InnerPaddingOwner
import kotlin.math.min


class BottomBar(context: Context) : FrameLayout(context), InnerPaddingOwner,
    CoordinatorLayout.AttachedBehavior {

    override val innerLargePadding: Int = 16.dp
    override val innerMediumPadding: Int = 16.dp
    override val innerSmallPadding: Int = 16.dp

    private val iconSize = 24.dp

    private val desireHeight = 56.dp

    init {
        minimumWidth = MATCH_PARENT
        minimumHeight = desireHeight
    }

    private val behavior = BottomBarBehavior()

    private val background = MaterialShapeDrawable().apply {
        initializeElevationOverlay(context)
        fillColor = ColorStateList.valueOf(Theme<Int, Color>(mainBarSurfaceColorKey))
    }

    private val navigationIcon = ImageView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        minimumHeight = iconSize
        minimumWidth = iconSize
        background = RippleDrawable(ColorStateList.valueOf(0x52000000), null, null)
        isClickable = true
        isFocusable = true
    }

    var title: String
        get() = titleView.text.toString()
        set(value) {
            ensureContainingTitle()
            titleView.text = value
        }

    private val titleView: TextView = TextView(context).apply {
        textSize = 18F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        isSingleLine = true
        maxLines = 1
        setTextColor(Color.Black.value)
    }

    val containsNavigationIcon: Boolean
        get() = contains(navigationIcon)

    val containsTitle: Boolean
        get() = contains(titleView)

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(titleView)
        }
    }

    private fun ensureContainingNavigationIcon() {
        if (!containsNavigationIcon) {
            addView(navigationIcon)
        }
    }

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)

        MaterialShapeUtils.setElevation(this, elevation)
    }

    override fun getElevation(): Float = background.elevation

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, background)
    }

    fun setNavigationIcon(icon: Drawable?) {
        ensureContainingNavigationIcon()
        navigationIcon.setImageDrawable(icon)
    }

    fun setNavigationIconTint(@ColorInt color: Int) {
        if (containsNavigationIcon) {
            navigationIcon.setColorFilter(color)
        }
    }

    fun setTitleColor(@ColorInt color: Int) {
        if (containsTitle) {
            titleView.setTextColor(color)
        }
    }

    override fun getBackground(): Drawable {
        return background
    }

    init {
        setBackground(background)
        elevation = 16F
        ensureContainingTitle()
        ensureContainingNavigationIcon()
    }

    fun setNavigationIconClickListener(listener: OnClickListener) {
        navigationIcon.setOnClickListener(listener)
    }

    fun show() {
        behavior.slideUp(view = this)
    }

    fun hide() {
        behavior.slideDown(view = this)
    }

    override fun getBehavior(): BottomBarBehavior = behavior

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec) // requires full width-size
        val widthMode = getMode(widthMeasureSpec)
        val heightSize = getSize(heightMeasureSpec)
        val heightMode = getMode(heightMeasureSpec)

        val width = when (widthMode) {
            EXACTLY -> widthSize
            AT_MOST -> min(suggestedMinimumWidth, widthSize)
            else -> widthSize
        } + paddingStart + paddingEnd

        val height = when (heightMode) {
            EXACTLY -> heightSize
            AT_MOST -> min(suggestedMinimumHeight, heightSize)
            else -> desireHeight
        } + paddingBottom + paddingTop

        setMeasuredDimension(width, height)

        val availableWidth = width - innerLargePadding - paddingStart - paddingEnd
        if (containsNavigationIcon) {
            navigationIcon.measure(
                makeMeasureSpec(iconSize, AT_MOST),
                makeMeasureSpec(iconSize, AT_MOST)
            )
        }
        if (containsTitle) {
            titleView.measure(
                makeMeasureSpec(availableWidth, AT_MOST),
                makeMeasureSpec(24.dp, AT_MOST)
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (containsNavigationIcon) {
            navigationIcon.layout(
                innerLargePadding - paddingStart,
                innerMediumPadding - paddingTop,
                innerLargePadding + navigationIcon.measuredWidth - paddingStart,
                height - innerMediumPadding - paddingBottom
            )
        }
        if (containsTitle) {
            titleView.layout(
                innerLargePadding + navigationIcon.measuredWidth - paddingStart + innerLargePadding + innerLargePadding,
                innerMediumPadding - paddingTop,
                innerLargePadding + navigationIcon.measuredWidth - paddingStart + innerLargePadding + innerLargePadding + titleView.measuredWidth,
                height - innerMediumPadding - paddingBottom
            )
        }
    }
}