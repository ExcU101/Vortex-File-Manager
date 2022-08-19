package io.github.excu101.vortex.ui.view.trail

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.RippleDrawable
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.contains
import androidx.core.view.updatePadding
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.Builder
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.*
import io.github.excu101.vortex.ui.view.dp
import io.github.excu101.vortex.ui.view.foundtation.InnerPaddingOwner

class TrailItemView(context: Context) : LinearLayout(context), InnerPaddingOwner {

    private val roundedBackground = MaterialShapeDrawable().apply {
        shapeAppearanceModel = Builder()
            .setTopLeftCorner(ROUNDED, 100F)
            .setTopRightCorner(ROUNDED, 100F)
            .setBottomLeftCorner(ROUNDED, 100F)
            .setBottomRightCorner(ROUNDED, 100F)
            .build()
        fillColor = valueOf(Theme<Int, Color>(trailSurfaceColorKey))
    }

    private var rippleDrawable = RippleDrawable(
        valueOf(Theme<Int, Color>(trailItemTitleTextColorKey)),
        roundedBackground,
        null
    )

    private val desireHeight = 36.dp
    private val minWidth = 36.dp

    override val innerLargePadding = 16.dp
    override val innerMediumPadding = 8.dp
    override val innerSmallPadding = 4.dp

    init {
        minimumWidth = minWidth
        minimumHeight = desireHeight
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

    fun setTitle(value: String) {
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
            rippleDrawable.setColor(valueOf(Theme<Int, Color>(trailItemRippleSelectedTintColorKey)))
            title.setTextColor(Theme<Int, Color>(trailItemTitleSelectedTextColorKey))
            arrow.setColorFilter(Theme<Int, Color>(trailItemArrowSelectedTintColorKey))
        } else {
            rippleDrawable.setColor(valueOf(Theme<Int, Color>(trailItemRippleTintColorKey)))
            title.setTextColor(Theme<Int, Color>(trailItemTitleTextColorKey))
            arrow.setColorFilter(Theme<Int, Color>(trailItemArrowTintColorKey))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, 48.dp)
    }

}