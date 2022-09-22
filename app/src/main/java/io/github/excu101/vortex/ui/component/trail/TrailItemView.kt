package io.github.excu101.vortex.ui.component.trail

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.RippleDrawable
import android.view.Gravity.CENTER
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.contains
import androidx.core.view.updatePadding
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeColorChangeListener
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.R
import io.github.excu101.vortex.ui.component.ThemeDp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.foundtation.InnerPaddingOwner
import io.github.excu101.vortex.ui.component.theme.key.*

class TrailItemView(context: Context) : ThemeLinearLayout(context), InnerPaddingOwner,
    ThemeColorChangeListener {

    companion object {
        private const val TITLE_INDEX = 0
        private const val ARROW_INDEX = 1
    }

    private val shape = MaterialShapeDrawable().apply {
        setTint(ThemeColor(trailSurfaceColorKey))
        initializeElevationOverlay(context)
        setUseTintColorForShadow(true)
    }


    private var background = RippleDrawable(
        valueOf(ThemeColor(trailItemTitleTextColorKey)),
        shape,
        null
    )

    private var isItemSelected = false

    private val minWidth = 36.dp
    private val desireHeight = ThemeDp(trailItemHeightKey)

    override val innerLargePadding = 16.dp
    override val innerMediumPadding = 8.dp
    override val innerSmallPadding = 4.dp

    init {
        gravity = CENTER
        minimumHeight = desireHeight
        isClickable = true
        isFocusable = true
        setBackground(background)
    }

    override fun setElevation(elevation: Float) {
        MaterialShapeUtils.setElevation(this, elevation)
    }

    private val title = TextView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        textSize = 16F
        updatePadding(left = innerMediumPadding, right = innerMediumPadding)
    }

    private val arrow = ImageView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setImageResource(R.drawable.ic_arrow_right_24)
    }

    private fun ensureContainingTitle() {
        if (!contains(title)) {
            addView(title, TITLE_INDEX)
        }
    }

    private fun ensureContainingArrow() {
        if (!contains(arrow)) {
            addView(arrow, ARROW_INDEX)
        }
    }

    fun setTitle(value: String? = null) {
        ensureContainingTitle()
        title.text = value
    }

    fun setArrowVisibility(isVisible: Boolean) {
        ensureContainingArrow()
        arrow.visibility = if (isVisible) {
            VISIBLE
        } else {
            GONE
        }
    }

    fun updateSelection(isSelected: Boolean) {
        isItemSelected = isSelected
        updateSelectionState()
    }

    fun updateSelectionState() {
        if (isItemSelected) {
            background.setColor(valueOf(ThemeColor(trailItemRippleSelectedTintColorKey)))
            title.setTextColor(ThemeColor(trailItemTitleSelectedTextColorKey))
            arrow.setColorFilter(ThemeColor(trailItemArrowSelectedTintColorKey))
        } else {
            background.setColor(valueOf(ThemeColor(trailItemRippleTintColorKey)))
            title.setTextColor(ThemeColor(trailItemTitleTextColorKey))
            arrow.setColorFilter(ThemeColor(trailItemArrowTintColorKey))
        }
    }

    override fun onChanged() {
        updateSelectionState()
        shape.setTint(ThemeColor(trailSurfaceColorKey))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }


//    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
//        val widthSize = MeasureSpec.getSize(widthSpec)
//        val widthMode = MeasureSpec.getMode(widthSpec)
//        val heightSize = MeasureSpec.getSize(heightSpec)
//        val heightMode = MeasureSpec.getMode(heightSpec)
//
//        val width = when (widthMode) {
//            MeasureSpec.EXACTLY -> widthSize
//            MeasureSpec.AT_MOST -> min(suggestedMinimumWidth, widthSize)
//            else -> widthSize
//        } + paddingStart + paddingEnd
//
//        val height = when (heightMode) {
//            MeasureSpec.EXACTLY -> heightSize
//            MeasureSpec.AT_MOST -> min(suggestedMinimumHeight, heightSize)
//            else -> suggestedMinimumHeight
//        } + paddingBottom + paddingTop
//
//        setMeasuredDimension(width, height)
//
//        val availableWidth = width - paddingStart - paddingRight
//
//    }

}