package io.github.excu101.vortex.ui.component.trail

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.RippleDrawable
import android.view.Gravity.CENTER
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.contains
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeColorChangeListener
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.R
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.ThemeUDp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.foundtation.InnerPaddingOwner
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.theme.key.*

class TrailItemView(context: Context) : ThemeLinearLayout(context),
    InnerPaddingOwner,
    ThemeColorChangeListener,
    ViewHolder.RecyclableView<PathItem> {

    companion object {
        private const val TITLE_INDEX = 0
        private const val ARROW_INDEX = 1
    }

    private val surface = MaterialShapeDrawable(
        builder().setAllCorners(
            ROUNDED, 16F.dp
        ).build()
    ).apply {
        setTint(ThemeColor(trailSurfaceColorKey))
        initializeElevationOverlay(context)
    }

    private val foreground = RippleDrawable(
        valueOf(ThemeColor(trailItemTitleTextColorKey)),
        surface,
        null,
    )

    var isArrowVisible: Boolean
        get() = arrow.isVisible
        set(value) {
            setArrowVisibility(value)
        }

    private val desireHeight = ThemeUDp(trailItemHeightKey)

    override val innerLargePadding = 16.dp
    override val innerMediumPadding = 8.dp
    override val innerSmallPadding = 4.dp

    init {
        id = ViewIds.Storage.Trail.RootId
        gravity = CENTER
        minimumHeight = desireHeight
        isClickable = true
        isFocusable = true
        setBackground(foreground)
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

    override fun onBind(item: PathItem) {
        setTitle(value = item.name)
    }

    override fun onBindSelection(isSelected: Boolean) {
        updateStateLists()
        setSelected(isSelected)
    }

    override fun onUnbind() {
        setTitle(null)
        setArrowVisibility(false)
    }

    override fun onBindListener(listener: OnClickListener) {
        setOnClickListener(listener)
    }

    override fun onBindLongListener(listener: OnLongClickListener) {
        setOnLongClickListener(listener)
    }

    override fun onUnbindListeners() {
        setOnClickListener(null)
        setOnLongClickListener(null)
    }

    fun setArrowVisibility(isVisible: Boolean) {
        ensureContainingArrow()
        arrow.visibility = if (isVisible) {
            VISIBLE
        } else {
            GONE
        }
    }

    private fun updateStateLists() {
        title.setTextColor(createTitleStateList())
        arrow.imageTintList = createArrowStateList()
        foreground.setColor(createRippleStateList())
        surface.setTint(ThemeColor(trailSurfaceColorKey))
    }

    override fun onColorChanged() {
        updateStateLists()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        MaterialShapeUtils.setParentAbsoluteElevation(this, surface)
    }

    private fun createRippleStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
                ThemeColor(trailItemRippleSelectedTintColorKey),
                ThemeColor(trailItemRippleTintColorKey),
            )
        )
    }

    private fun createArrowStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
                ThemeColor(trailItemArrowSelectedTintColorKey),
                ThemeColor(trailItemArrowTintColorKey),
            )
        )
    }

    private fun createTitleStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
                ThemeColor(trailItemTitleSelectedTextColorKey),
                ThemeColor(trailItemTitleTextColorKey),
            )
        )
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