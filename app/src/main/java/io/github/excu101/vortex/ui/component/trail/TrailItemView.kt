package io.github.excu101.vortex.ui.component.trail

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.contains
import androidx.core.view.isVisible
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.manager.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.R
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.ThemeDp
import io.github.excu101.vortex.ui.component.ThemeMaterialShapeDrawable
import io.github.excu101.vortex.ui.component.ThemeUDp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder.RecyclableView
import io.github.excu101.vortex.ui.component.theme.key.*

class TrailItemView(
    context: Context,
) : ThemeFrameLayout(context), RecyclableView<PathItem> {

    private val leftPadding = ThemeDp(trailItemLeftPaddingKey)
    private val rightPadding = ThemeDp(trailItemRightPaddingKey)

    private val surface = ThemeMaterialShapeDrawable(
        builder = {
            setAllCorners(
                ROUNDED, 16F.dp
            )
        },
        colorKey = trailSurfaceColorKey
    )

    var title: CharSequence?
        get() = titleView.text
        set(value) {
            if (value == titleView.text) return
            ensureContainingTitle()
            titleView.text = value
        }

    var isArrowVisible: Boolean
        get() = arrowView.isVisible
        set(value) {
            if (value == arrowView.isVisible) return
            ensureContainingArrow()
            arrowView.isVisible = value
        }

    init {
        id = ViewIds.Storage.Trail.RootId
        isClickable = true
        isFocusable = true
        background = surface
        foreground = createRippleForeground()
    }

    private val titleView = TextView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        textSize = 16F
    }

    private val arrowView = ImageView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setImageResource(R.drawable.ic_arrow_right_24)
    }

    private fun ensureContainingTitle() {
        if (!contains(titleView)) {
            addView(titleView)
        }
    }

    private fun ensureContainingArrow() {
        if (!contains(arrowView)) {
            addView(arrowView)
        }
    }

    override fun onBind(item: PathItem) {
        title = item.name
    }

    override fun onBindSelection(isSelected: Boolean) {
        updateStateLists()
        setSelected(isSelected)
    }

    override fun onBindPayload(payload: Any?) {
        if (payload is Boolean) {
            isArrowVisible = payload
        }
    }

    override fun onUnbind() {
        title = null
        isArrowVisible = false
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

    private fun updateStateLists() {
        titleView.setTextColor(createTitleStateList())
        arrowView.imageTintList = createArrowStateList()
        foreground = createRippleForeground()
        surface.setTint(ThemeColor(trailSurfaceColorKey))
    }

    private fun createRippleForeground() = RippleDrawable(
        createRippleStateList(),
        null,
        surface,
    )

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

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var width = rightPadding + leftPadding
        val height = ThemeUDp(trailItemHeightKey)

        if (!title.isNullOrEmpty()) {
            measureChild(
                titleView,
                widthSpec,
                heightSpec
            )
            width += titleView.measuredWidth
        }

        if (arrowView.isVisible) {
            measureChild(
                arrowView,
                widthSpec,
                heightSpec
            )
            width += arrowView.measuredWidth
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var width = 0

        if (!title.isNullOrEmpty()) {
            width += 8.dp
            titleView.layout(
                width,
                getVerticalPadding(titleView),
                width + titleView.measuredWidth,
                getVerticalPadding(titleView) + titleView.measuredHeight
            )
            width += titleView.measuredWidth
        }

        if (isArrowVisible) {
            width += 8.dp
            arrowView.layout(
                width,
                getVerticalPadding(arrowView),
                width + arrowView.measuredWidth,
                getVerticalPadding(arrowView) + arrowView.measuredHeight
            )
        }
    }

    private fun getVerticalPadding(
        view: View,
    ): Int {
        return (height - view.measuredHeight) / 2
    }

    private fun getHorizontalPadding(
        view: View,
    ): Int {
        return (width - view.measuredWidth) / 2
    }

}