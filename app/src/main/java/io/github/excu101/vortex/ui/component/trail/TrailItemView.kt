package io.github.excu101.vortex.ui.component.trail

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.RippleDrawable
import android.view.Gravity.CENTER
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.contains
import androidx.core.view.updatePadding
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.Builder
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeDimen
import io.github.excu101.vortex.R
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.foundtation.InnerPaddingOwner
import io.github.excu101.vortex.ui.component.theme.key.*

class TrailItemView(context: Context) : LinearLayout(context), InnerPaddingOwner {

    private val roundedBackground = MaterialShapeDrawable(
        Builder()
            .setTopLeftCorner(ROUNDED, 100F)
            .setTopRightCorner(ROUNDED, 100F)
            .setBottomLeftCorner(ROUNDED, 100F)
            .setBottomRightCorner(ROUNDED, 100F)
            .build()
    ).apply {
        fillColor = valueOf(ThemeColor(trailSurfaceColorKey))
    }

    private var rippleDrawable = RippleDrawable(
        valueOf(ThemeColor(trailItemTitleTextColorKey)),
        roundedBackground,
        null
    )

    private val minWidth = 36.dp

    override val innerLargePadding = 16.dp
    override val innerMediumPadding = 8.dp
    override val innerSmallPadding = 4.dp

    init {
        minimumWidth = minWidth
        minimumHeight = ThemeDimen(trailHeightKey).dp
        gravity = CENTER
        isClickable = true
        isFocusable = true
        background = rippleDrawable
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
            addView(title)
        }
    }

    private fun ensureContainingArrow() {
        if (!contains(arrow)) {
            addView(arrow)
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
        if (isSelected) {
            rippleDrawable.setColor(valueOf(ThemeColor(trailItemRippleSelectedTintColorKey)))
            title.setTextColor(ThemeColor(trailItemTitleSelectedTextColorKey))
            arrow.setColorFilter(ThemeColor(trailItemArrowSelectedTintColorKey))
        } else {
            rippleDrawable.setColor(valueOf(ThemeColor(trailItemRippleTintColorKey)))
            title.setTextColor(ThemeColor(trailItemTitleTextColorKey))
            arrow.setColorFilter(ThemeColor(trailItemArrowTintColorKey))
        }
    }

}