package io.github.excu101.vortex.ui.component.storage.standard.linear

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.ColorStateList.valueOf
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.makeMeasureSpec
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
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.StorageCellBadgeIcon
import io.github.excu101.vortex.ui.component.ThemeDp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.storage.RecyclableStorageCell
import io.github.excu101.vortex.ui.component.theme.key.storageListItemHorizontalInfoPaddingKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemHorizontalTitlePaddingKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemIconBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemIconTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemLinearHeightDimenKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemLinearWidthDimenKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemSecondaryTextColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemSurfaceColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemSurfaceRippleColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemTitleTextColorKey
import io.github.excu101.vortex.ui.component.themeMeasure


class StandardStorageLinearCell(context: Context) : ThemeFrameLayout(context),
    RecyclableStorageCell {

    private val largeInnerPadding = 16.dp
    private val middleInnerPadding = 8.dp

    private val titlePadding = ThemeDp(storageListItemHorizontalTitlePaddingKey)
    private val infoPadding = ThemeDp(storageListItemHorizontalInfoPaddingKey)

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
        tintList = createSurfaceStateList()
    }

    private val foreground = RippleDrawable(
        createRippleStateList(),
        null,
        null
    )

    private val iconBadge = StorageCellBadgeIcon(iconSize, iconSize).apply {
        duration = 500L
    }

    private val iconView = object : ImageView(context) {
        override fun draw(canvas: Canvas) {
            super.draw(canvas)
            iconBadge.draw(canvas)
        }
    }.apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        scaleType = ScaleType.CENTER_INSIDE
        id = ViewIds.Storage.Item.IconId
        minimumWidth = iconSize
        minimumHeight = iconSize
        background = iconSurface
        // TODO: Replace with FrameLayout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            foreground = iconForeground
        }
        imageTintList = createIconStateList()
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
            iconBadge.isSelected = value
            isSelected = value
        }

    override var isBookmarked: Boolean = false
        set(value) {
            iconBadge.isBookmarked = value
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

        setBackground(surface)
        setForeground(foreground)
        updateStateLists()
    }


    fun setOnIconClickListener(listener: OnClickListener?) {
        iconView.setOnClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: OnLongClickListener?) {
        iconView.setOnLongClickListener(listener)
    }

    override fun setBackgroundTintList(tint: ColorStateList?) {
        surface.tintList = tint
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (width, height) = themeMeasure(
            widthMeasureSpec,
            heightMeasureSpec,
            widthKey = storageListItemLinearWidthDimenKey,
            heightKey = storageListItemLinearHeightDimenKey
        )

        setMeasuredDimension(width, height)

        val availableWidth = width - largeInnerPadding
        if (containsIcon) {
            iconView.measure(makeMeasureSpec(iconSize, AT_MOST), makeMeasureSpec(iconSize, AT_MOST))
        }
        if (containsTitle) {
            titleView.measure(
                makeMeasureSpec(availableWidth, AT_MOST),
                makeMeasureSpec(24.dp, AT_MOST)
            )
        }
        if (containsInfo) {
            infoView.measure(
                makeMeasureSpec(availableWidth, AT_MOST),
                makeMeasureSpec(20.dp, AT_MOST)
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var widthLeft = largeInnerPadding
        if (containsIcon) {
            iconView.layout(
                widthLeft,
                middleInnerPadding,
                widthLeft + iconView.measuredWidth,
                height - middleInnerPadding
            )
            widthLeft += iconView.measuredWidth
        }
        if (containsTitle) {
            titleView.layout(
                widthLeft + titlePadding,
                middleInnerPadding,
                widthLeft + titlePadding + titleView.measuredWidth,
                middleInnerPadding + 3.dp + titleView.lineHeight
            )
        }
        if (containsInfo) {
            infoView.layout(
                widthLeft + infoPadding,
                middleInnerPadding + titleView.lineHeight,
                widthLeft + infoPadding + infoView.measuredWidth,
                middleInnerPadding + 3.dp + titleView.lineHeight + infoView.lineHeight
            )
        }
    }

    override fun onBind(item: PathItem) = with(item) {
        onColorChanged()
        super.onBind(item)
        isBookmarked = bookmarkExists
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
        foreground.setColor(createRippleStateList())
        titleView.setTextColor(createTitleStateList())
        infoView.setTextColor(createInfoStateList())
        iconView.imageTintList = createIconStateList()
        iconSurface.tintList = createIconSurfaceStateList()
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
                ThemeColor(storageListItemIconTintColorKey)
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
                ThemeColor(storageListItemIconBackgroundColorKey)
            )
        )
    }

}