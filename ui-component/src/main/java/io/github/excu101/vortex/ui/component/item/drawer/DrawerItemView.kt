package io.github.excu101.vortex.ui.component.item.drawer

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearanceModel.builder
import com.google.android.material.shape.Shapeable
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.ItemViewIds
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder.RecyclableView
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.themeMeasure

class DrawerItemView(
    context: Context,
) : ThemeFrameLayout(context), Shapeable, RecyclableView<DrawerItem> {

    private val iconHorizontalPadding = 16.dp
    private val titleHorizontalPadding = 32.dp
    private val additionalTitleViewHeight = 2.dp
    private val iconSize = 24.dp

    private val rippleTintList = ColorStateList(
        arrayOf(
//            intArrayOf(android.R.attr.state_focused, android.R.attr.state_selected),
            intArrayOf()
        ),
        intArrayOf(
//            ThemeColor(trailItemRippleSelectedTintColorKey),
            ThemeColor(trailItemRippleTintColorKey)
        )
    )

    private val surface = MaterialShapeDrawable(
        builder().setAllCorners(ROUNDED, 0F.dp).build()
    ).apply {
        tintList = createShapeColorStateList()
    }

    private val iconView = ImageView(context).apply {
        imageTintList = createIconColorStateList()
    }

    private val titleView = TextView(context).apply {
        textSize = 16F
        setTextColor(createTitleColorStateList())
    }

    var icon: Drawable?
        get() = iconView.drawable
        set(value) {
            iconView.setImageDrawable(value)
        }

    private val isEmptyIcon: Boolean
        get() = icon == null

    var title: CharSequence?
        get() = titleView.text
        set(value) {
            titleView.text = value
        }

    init {
        id = ItemViewIds.DrawerItem
        isClickable = true
        isFocusable = true

        addView(iconView)
        addView(titleView)

        background = surface
        foreground = createForegroundDrawable()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, surface)
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return surface.shapeAppearanceModel
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        surface.shapeAppearanceModel = shapeAppearanceModel
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val (width, height) = themeMeasure(
            widthMeasureSpec,
            heightMeasureSpec,
            drawerItemWidthKey,
            drawerItemHeightKey
        )

        setMeasuredDimension(width, height)
        val availableWidth = width - iconHorizontalPadding

        if (!isEmptyIcon) {
            iconView.measure(
                MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.AT_MOST)
            )
        }
        titleView.measure(
            MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.AT_MOST)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var widthLeft = iconHorizontalPadding
        if (!isEmptyIcon) {
            iconView.layout(
                widthLeft,
                height / 2 - iconView.measuredHeight,
                widthLeft + iconSize,
                height / 2 + iconView.measuredHeight
            )
            widthLeft += iconSize
        }

        if (!isEmptyIcon) {
            widthLeft += titleHorizontalPadding
        }
        titleView.layout(
            widthLeft,
            (height - titleView.lineHeight) / 2,
            widthLeft + titleView.measuredWidth,
            (height - titleView.lineHeight) / 2 + titleView.lineHeight + additionalTitleViewHeight
        )
    }

    override fun onChanged() {
        iconView.imageTintList = createIconColorStateList()
        surface.tintList = createShapeColorStateList()
        titleView.setTextColor(createTitleColorStateList())
    }

    override fun onBind(item: DrawerItem) {
        onChanged()
        icon = item.value.icon
        title = item.value.title
    }

    override fun onUnbind() {
        title = null
        icon = null
    }

    override fun onBindListener(listener: OnClickListener) {
        setOnClickListener(listener)
    }

    override fun onBindSelection(isSelected: Boolean) {
        setSelected(isSelected)
        onChanged()
    }

    override fun onUnbindListeners() {
        setOnClickListener(null)
    }

    private fun createForegroundDrawable(): RippleDrawable {
        return RippleDrawable(
            rippleTintList,
            null,
            null
        )
    }

    private fun createIconColorStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf()
            ),
            intArrayOf(
                ThemeColor(mainDrawerItemIconSelectedTintColorKey),
                ThemeColor(mainDrawerItemIconTintColorKey)
            )
        )
    }

    private fun createShapeColorStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf()
            ),
            intArrayOf(
                ThemeColor(mainDrawerItemSelectedBackgroundColorKey),
                ThemeColor(mainDrawerItemBackgroundColorKey)
            )
        )
    }

    private fun createTitleColorStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf()
            ),
            intArrayOf(
                ThemeColor(mainDrawerItemTitleSelectedTextColorKey),
                ThemeColor(mainDrawerItemTitleTextColorKey)
            )
        )
    }

}