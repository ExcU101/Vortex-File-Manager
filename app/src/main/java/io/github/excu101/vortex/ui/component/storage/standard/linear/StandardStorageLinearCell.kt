package io.github.excu101.vortex.ui.component.storage.standard.linear

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.text.TextUtils
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
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.manager.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.ThemeDp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.storage.RecyclableStorageCell
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.themeMeasure
import io.github.excu101.vortex.utils.icon


class StandardStorageLinearCell(context: Context) : ThemeFrameLayout(context),
    RecyclableStorageCell {

    private val largeInnerPadding = 16.dp
    private val middleInnerPadding = 8.dp

    private val titlePadding = ThemeDp(storageListItemHorizontalTitlePaddingKey)
    private val infoPadding = ThemeDp(storageListItemHorizontalSubtitlePaddingKey)

    private val iconSize = 40.dp

    private val containsTitle: Boolean
        get() = contains(titleView)

    private val containsInfo: Boolean
        get() = contains(infoView)

    private val containsIcon: Boolean
        get() = contains(iconView)

    private val iconSurface = MaterialShapeDrawable(
        builder().setAllCorners(ROUNDED, 500F).build()
    ).apply {

    }

    private val surface = MaterialShapeDrawable(
        builder().build()
    ).apply {
        initializeElevationOverlay(context)
        tintList = createSurfaceStateList()
    }

    private val iconView = ImageView(context).apply {
        layoutParams = LayoutParams(iconSize, iconSize)
        scaleType = ScaleType.CENTER_INSIDE
        id = ViewIds.Storage.Item.IconId

        background = createIconRippleBackground()
    }

    private val titleView = TextView(context).apply {
        textSize = 16F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        isSingleLine = true
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
    }

    private val infoView = TextView(context).apply {
        textSize = 14F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        isSingleLine = true
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
    }

    override var title: String?
        get() = titleView.text.toString()
        set(value) {
            ensureContainingTitle()
            titleView.text = value
        }

    override var info: String?
        get() = infoView.text.toString()
        set(value) {
            ensureContainingInfo()
            infoView.text = value
        }

    override var icon: Drawable?
        get() = iconView.drawable
        set(value) {
            ensureContainingIcon()
            iconView.setImageDrawable(value)
        }

    override var isCellSelected: Boolean
        get() = isSelected
        set(value) {
            isSelected = value
        }

    override var isBookmarked: Boolean = false
        set(value) {
            field = value
        }

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(titleView)
        }
    }

    private fun ensureContainingInfo() {
        if (!containsInfo) {
            addView(infoView)
        }
    }

    private fun ensureContainingIcon() {
        if (!containsIcon) {
            addView(iconView)
        }
    }

    fun setIcon(@DrawableRes id: Int) {
        ensureContainingIcon()
        iconView.setImageResource(id)
    }

    override fun onColorChanged() {
        updateStateLists()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, surface)
    }

    init {
        id = ViewIds.Storage.Item.RootId
        isClickable = true
        isFocusable = true
        clipToOutline = true

        background = surface
        foreground = createRippleForeground()
        updateStateLists()
    }

    private fun createIconRippleBackground(): RippleDrawable {
        return RippleDrawable(
            ColorStateList.valueOf(ThemeColor(storageListItemIconTintColorKey)),
            iconSurface,
            null
        )
    }

    fun setOnIconClickListener(listener: OnClickListener?) {
        iconView.setOnClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: OnLongClickListener?) {
        iconView.setOnLongClickListener(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (width, height) = themeMeasure(
            widthSpec = widthMeasureSpec,
            heightSpec = heightMeasureSpec,
            widthKey = storageListItemLinearWidthDimenKey,
            heightKey = storageListItemLinearHeightDimenKey
        )

        setMeasuredDimension(width, height)

        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var widthLeft = largeInnerPadding
        if (containsIcon) {
            iconView.layout(
                widthLeft,
                (measuredHeight - iconView.measuredHeight) shr 1,
                widthLeft + iconView.measuredWidth,
                ((measuredHeight - iconView.measuredHeight) shr 1) + iconView.measuredHeight
            )
            widthLeft += iconView.measuredWidth
        }
        if (containsTitle) {
            titleView.layout(
                widthLeft + titlePadding,
                middleInnerPadding,
                widthLeft + titlePadding + titleView.measuredWidth,
                middleInnerPadding + titleView.measuredHeight
            )
        }
        if (containsInfo) {
            infoView.layout(
                widthLeft + infoPadding,
                middleInnerPadding + titleView.measuredHeight,
                widthLeft + infoPadding + infoView.measuredWidth,
                middleInnerPadding + titleView.measuredHeight + infoView.measuredHeight
            )
        }
    }

    override fun onBind(item: PathItem) = with(item) {
        super.onBind(item)
        this@StandardStorageLinearCell.icon = icon
        onColorChanged()
    }

    override fun onBindPayload(payload: Any?) {
        if (payload is Boolean) {
            isBookmarked = payload
        }
    }

    override fun onUnbind() {
        titleView.text = null
        infoView.text = null
        iconView.setImageDrawable(null)
    }

    override fun onBindSelection(isSelected: Boolean) {
        isCellSelected = isSelected
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
        titleView.setTextColor(createTitleStateList())
        infoView.setTextColor(createInfoStateList())
        iconView.imageTintList = createIconStateList()
        iconSurface.tintList = createIconSurfaceStateList()
    }

    private fun createRippleForeground(): RippleDrawable {
        return RippleDrawable(
            createRippleStateList(),
            null,
            null
        )
    }

    private fun createSurfaceStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
//                intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
//                ThemeColor(storageListItemSurfaceSelectedColorKey),
                ThemeColor(storageListItemSurfaceColorKey)
            )
        )
    }

    private fun createRippleStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
//                intArrayOf(android.R.attr.state_selected),
                intArrayOf() // default
            ),
            intArrayOf(
//                ThemeColor(storageListItemSurfaceRippleSelectedColorKey),
                ThemeColor(storageListItemSurfaceRippleColorKey) // default
            )
        )
    }

    private fun createTitleStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
//                intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
//                ThemeColor(storageListItemTitleSelectedTextColorKey),
                ThemeColor(storageListItemTitleTextColorKey)
            )
        )
    }

    private fun createInfoStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
//            intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
//                ThemeColor(storageListItemSecondarySelectedTextColorKey),
                ThemeColor(storageListItemSecondaryTextColorKey)
            )
        )
    }

    private fun createIconStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
//                intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
//                ThemeColor(storageListItemIconSelectedTintColorKey),
                if (isBookmarked) ThemeColor(
                    storageListItemIconBookmarkedColorKey
                )
                else ThemeColor(
                    storageListItemIconTintColorKey
                )
            )
        )
    }

    private fun createIconSurfaceStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
//            intArrayOf(android.R.attr.state_selected),
                intArrayOf(),
            ),
            intArrayOf(
//                ThemeColor(storageListItemIconBackgroundSelectedColorKey),
                if (isBookmarked) ThemeColor(
                    storageListItemIconBackgroundBookmarkedColorKey
                )
                else ThemeColor(
                    storageListItemIconBackgroundColorKey
                )
            )
        )
    }

}