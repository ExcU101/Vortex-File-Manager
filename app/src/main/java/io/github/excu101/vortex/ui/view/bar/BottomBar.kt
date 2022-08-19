package io.github.excu101.vortex.ui.view.bar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View.MeasureSpec.*
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.contains
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.mainBarSurfaceColorKey
import io.github.excu101.vortex.ui.theme.key.mainBarTitleTextColorKey
import io.github.excu101.vortex.ui.view.dp
import io.github.excu101.vortex.ui.view.foundtation.InnerPaddingOwner
import kotlin.math.min

class BottomBar(context: Context) : ViewGroup(context), InnerPaddingOwner {

    override val innerLargePadding: Int = 16.dp
    override val innerMediumPadding: Int = 16.dp
    override val innerSmallPadding: Int = 16.dp

    private val iconSize = 24.dp

    private val desireHeight = 56.dp

    private val background = MaterialShapeDrawable().apply {
        initializeElevationOverlay(context)
        fillColor = ColorStateList.valueOf(Theme<Int, Color>(mainBarSurfaceColorKey))
    }

    private val navigation = ImageView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    private val title: TextView = TextView(context).apply {
        textSize = 18F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(Theme<Int, Color>(mainBarTitleTextColorKey))
    }

    val containsTitle: Boolean
        get() = contains(title)

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(title)
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

    fun setTitle(message: String) {
        ensureContainingTitle()
        title.text = message
    }

    fun setTitleColor(@ColorInt color: Int) {
        if (containsTitle) {
            title.setTextColor(color)
        }
    }

    override fun getBackground(): Drawable {
        return background
    }

    init {
        setBackground(background)
        elevation = 16F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getSize(widthMeasureSpec) + paddingStart + paddingEnd
        val height = min(desireHeight, getSize(heightMeasureSpec)) + paddingBottom + paddingTop

        setMeasuredDimension(width, height)

        val availableWidth = width - innerLargePadding
        title.measure(makeMeasureSpec(availableWidth, AT_MOST), makeMeasureSpec(24.dp, AT_MOST))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        title.layout(
            innerLargePadding,
            (height - title.lineHeight) / 2,
            innerLargePadding + title.measuredWidth,
            (height - title.lineHeight) / 2 + title.lineHeight
        )
    }
}