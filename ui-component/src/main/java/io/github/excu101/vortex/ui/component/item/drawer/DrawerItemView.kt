package io.github.excu101.vortex.ui.component.item.drawer

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.RippleDrawable
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearanceModel.builder
import com.google.android.material.shape.Shapeable
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.theme.key.drawerItemHeightKey
import io.github.excu101.vortex.theme.key.drawerItemWidthKey
import io.github.excu101.vortex.theme.key.mainDrawerItemBackgroundColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemIconSelectedTintColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemIconTintColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemSelectedBackgroundColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemTitleSelectedTextColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemTitleTextColorKey
import io.github.excu101.vortex.theme.key.trailItemRippleTintColorKey
import io.github.excu101.vortex.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.ItemViewIds
import io.github.excu101.vortex.ui.component.ddp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder.RecyclableView
import io.github.excu101.vortex.ui.component.themeMeasure

class DrawerItemView(
    context: Context,
) : ThemeFrameLayout(context), Shapeable, RecyclableView<DrawerItem> {

    private val iconHorizontalPadding = 16.dp
    private val titleHorizontalPadding = 32.dp
    private val iconSize = 24.dp

    private val rippleTintList = ColorStateList(
        arrayOf(
            intArrayOf()
        ),
        intArrayOf(
            ThemeColor(trailItemRippleTintColorKey)
        )
    )

    private val surface = MaterialShapeDrawable(
        builder().setAllCorners(ROUNDED, 8F.dp).build()
    ).apply {
        setBounds(4.ddp, 4.ddp, 4.ddp, 4.ddp)
        tintList = createShapeColorStateList()
    }

    private val iconView = ImageView(context).apply {
        imageTintList = createIconColorStateList()
        layoutParams = LayoutParams(24.dp, 24.dp)
    }

    private val titleView = TextView(context).apply {
        textSize = 16F
        setTextColor(createTitleColorStateList())
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
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
        themeMeasure(
            widthMeasureSpec,
            heightMeasureSpec,
            drawerItemWidthKey,
            drawerItemHeightKey,
            ::setMeasuredDimension
        )

        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var widthLeft = iconHorizontalPadding
        if (!isEmptyIcon) {
            iconView.layout(
                widthLeft,
                (measuredHeight - iconView.measuredHeight) shr 1,
                widthLeft + iconView.measuredWidth,
                ((measuredHeight - iconView.measuredHeight) shr 1) + iconView.measuredHeight
            )
            widthLeft += iconSize
        }

        if (!isEmptyIcon) {
            widthLeft += titleHorizontalPadding
        }

        titleView.layout(
            widthLeft,
            (height - titleView.lineHeight) shr 1,
            widthLeft + titleView.measuredWidth,
            ((height - titleView.lineHeight) shr 1) + titleView.measuredHeight
        )
    }

    override fun onColorChanged() {
        iconView.imageTintList = createIconColorStateList()
        surface.tintList = createShapeColorStateList()
        titleView.setTextColor(createTitleColorStateList())
    }

    override fun onBind(item: DrawerItem) {
        id = item.value.id
        if (item.attrs.iconColor != null && item.attrs.textColor != null) {
            iconView.setColorFilter(item.attrs.iconColor)
            titleView.setTextColor(item.attrs.textColor)
        } else {
            onColorChanged()
        }
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
        onColorChanged()
    }

    override fun onUnbindListeners() {
        setOnClickListener(null)
    }

    private fun createForegroundDrawable(): RippleDrawable {
        return RippleDrawable(
            rippleTintList,
            null,
            InsetDrawable(surface, 4.dp)
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