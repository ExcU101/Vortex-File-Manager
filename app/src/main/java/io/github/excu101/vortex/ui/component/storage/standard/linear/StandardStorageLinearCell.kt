package io.github.excu101.vortex.ui.component.storage.standard.linear

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.View.MeasureSpec.*
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.contains
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ViewIds.StorageListItem.iconId
import io.github.excu101.vortex.ViewIds.StorageListItem.rootId
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.ThemeDp
import io.github.excu101.vortex.ui.component.ThemeUDp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder.RecyclableView
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemNameKey
import kotlin.math.min


class StandardStorageLinearCell(context: Context) : ThemeFrameLayout(context),
    RecyclableView<PathItem> {

    private val largeInnerPadding = 16.dp
    private val middleInnerPadding = 8.dp

    private val titlePadding = ThemeDp(storageListItemHorizontalTitlePaddingKey)
    private val infoPadding = ThemeDp(storageListItemHorizontalInfoPaddingKey)

    private val desireWidth = ThemeUDp(storageListItemLinearWidthDimenKey)
    private val desireHeight = ThemeUDp(storageListItemLinearHeightDimenKey)

    private val iconSize = 40.dp

    private val containsTitle: Boolean
        get() = contains(title)

    private val containsInfo: Boolean
        get() = contains(info)

    private val containsIcon: Boolean
        get() = contains(icon)

    private val iconSurface = MaterialShapeDrawable(
        builder().setAllCorners(ROUNDED, 500F).build()
    ).apply {

    }

    private val iconForeground = RippleDrawable(
        valueOf(ThemeColor(storageListItemIconTintColorKey)),
        null,
        iconSurface
    )

    private val surface = MaterialShapeDrawable(
        builder().setAllCorners(ROUNDED, 0F).build()
    ).apply {
//        shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
        initializeElevationOverlay(context)
    }

    private val foreground = RippleDrawable(
        createRippleStateList(),
        null,
        null
    )

    private val icon = ImageView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        scaleType = ScaleType.CENTER_INSIDE
        id = iconId
        minimumWidth = iconSize
        minimumHeight = iconSize
        isClickable = true
        isFocusable = true
        setBackground(iconSurface)
        // TODO: Replace with FrameLayout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setForeground(iconForeground)
        }
    }

    private val title = TextView(context).apply {
        textSize = 16F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    private val info = TextView(context).apply {
        textSize = 14F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
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

    override fun onChanged() {
        updateStateLists()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, surface)
    }

    init {
        id = rootId
        isClickable = true
        isFocusable = true
        clipToOutline = true

        setBackground(surface)
        setForeground(foreground)
        updateStateLists()
    }

    fun setOnIconClickListener(listener: OnClickListener?) {
        icon.setOnClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: OnLongClickListener?) {
        icon.setOnLongClickListener(listener)
    }

    override fun setBackgroundTintList(tint: ColorStateList?) {
        surface.tintList = tint
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec)
        val widthMode = getMode(widthMeasureSpec)
        val heightSize = getSize(heightMeasureSpec)
        val heightMode = getMode(heightMeasureSpec)

        val width = when (widthMode) {
            EXACTLY -> widthSize
            AT_MOST -> min(desireWidth, widthSize)
            else -> desireWidth
        }
        val height = when (heightMode) {
            EXACTLY -> heightSize
            AT_MOST -> min(desireHeight, heightSize)
            else -> desireHeight
        }

        setMeasuredDimension(width, height)

        val availableWidth = width - largeInnerPadding
        if (containsIcon) {
//            measureChild(icon, widthMeasureSpec, heightMeasureSpec)
            icon.measure(makeMeasureSpec(iconSize, AT_MOST), makeMeasureSpec(iconSize, AT_MOST))
        }
        if (containsTitle) {
//            measureChild(title, widthMeasureSpec, heightMeasureSpec)
            title.measure(makeMeasureSpec(availableWidth, AT_MOST), makeMeasureSpec(24.dp, AT_MOST))
        }
        if (containsInfo) {
//            measureChild(info, widthMeasureSpec, heightMeasureSpec)
            info.measure(makeMeasureSpec(availableWidth, AT_MOST), makeMeasureSpec(20.dp, AT_MOST))
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var widthLeft = largeInnerPadding
        if (containsIcon) {
            icon.layout(widthLeft,
                middleInnerPadding,
                widthLeft + icon.measuredWidth,
                height - middleInnerPadding)
            widthLeft += icon.measuredWidth
        }
        if (containsTitle) {
            title.layout(widthLeft + titlePadding,
                middleInnerPadding,
                widthLeft + titlePadding + title.measuredWidth,
                middleInnerPadding + 3.dp + title.lineHeight)
        }
        if (containsInfo) {
            info.layout(widthLeft + infoPadding,
                middleInnerPadding + title.lineHeight,
                widthLeft + infoPadding + info.measuredWidth,
                middleInnerPadding + 3.dp + title.lineHeight + info.lineHeight)
        }
    }

    override fun onBind(item: PathItem) {
        onChanged()
        setTitle(FormatterThemeText(key = fileListItemNameKey, item.name))
        setIcon(item.icon)
        setInfo(item.info)
    }

    override fun onUnbind() {
        title.text = null
        info.text = null
        icon.setImageDrawable(null)
    }

    override fun onBindSelection(isSelected: Boolean) {
        setSelected(isSelected)
    }

    override fun onBindListener(listener: OnClickListener) {
        setOnIconClickListener(listener)
        setOnClickListener(listener)
    }

    override fun onBindLongListener(listener: OnLongClickListener) {
        setOnIconLongClickListener(listener)
        setOnLongClickListener(listener)
    }

    override fun onUnbindListeners() {
        setOnClickListener(null)
        setOnIconClickListener(null)
        setOnLongClickListener(null)
        setOnIconLongClickListener(null)
    }

    private fun updateStateLists() {
        surface.tintList = createSurfaceStateList()
        foreground.setColor(createRippleStateList())
        title.setTextColor(createTitleStateList())
        info.setTextColor(createInfoStateList())
        icon.imageTintList = createIconStateList()
        iconSurface.tintList = createIconSurfaceStateList()
    }

    private fun createSurfaceStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
                ThemeColor(storageListItemSurfaceSelectedColorKey),
                ThemeColor(storageListItemSurfaceColorKey)
            )
        )
    }

    private fun createRippleStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf() // default
            ),
            intArrayOf(
                ThemeColor(storageListItemSurfaceRippleSelectedColorKey),
                ThemeColor(storageListItemSurfaceRippleColorKey) // default
            ))
    }

    private fun createTitleStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
                ThemeColor(storageListItemTitleSelectedTextColorKey),
                ThemeColor(storageListItemTitleTextColorKey)
            )
        )
    }

    private fun createInfoStateList(): ColorStateList {
        return ColorStateList(arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(),
        ),
            intArrayOf(ThemeColor(storageListItemSecondarySelectedTextColorKey),
                ThemeColor(storageListItemSecondaryTextColorKey)))
    }

    private fun createIconStateList(): ColorStateList {
        return ColorStateList(arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(),
        ),
            intArrayOf(ThemeColor(storageListItemIconSelectedTintColorKey),
                ThemeColor(storageListItemIconTintColorKey)))
    }

    private fun createIconSurfaceStateList(): ColorStateList {
        return ColorStateList(arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(),
        ),
            intArrayOf(ThemeColor(storageListItemIconBackgroundSelectedColorKey),
                ThemeColor(storageListItemIconBackgroundColorKey)))
    }

}