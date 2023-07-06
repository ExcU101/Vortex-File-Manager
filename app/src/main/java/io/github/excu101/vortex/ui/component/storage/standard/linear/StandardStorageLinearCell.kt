package io.github.excu101.vortex.ui.component.storage.standard.linear

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.text.TextUtils
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.contains
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.theme.key.storageListItemHorizontalSubtitlePaddingKey
import io.github.excu101.vortex.theme.key.storageListItemHorizontalTitlePaddingKey
import io.github.excu101.vortex.theme.key.storageListItemIconBackgroundBookmarkedColorKey
import io.github.excu101.vortex.theme.key.storageListItemIconBackgroundColorKey
import io.github.excu101.vortex.theme.key.storageListItemIconBookmarkedColorKey
import io.github.excu101.vortex.theme.key.storageListItemIconTintColorKey
import io.github.excu101.vortex.theme.key.storageListItemLinearHeightDimenKey
import io.github.excu101.vortex.theme.key.storageListItemLinearWidthDimenKey
import io.github.excu101.vortex.theme.key.storageListItemSecondaryTextColorKey
import io.github.excu101.vortex.theme.key.storageListItemSurfaceColorKey
import io.github.excu101.vortex.theme.key.storageListItemSurfaceRippleColorKey
import io.github.excu101.vortex.theme.key.storageListItemTitleTextColorKey
import io.github.excu101.vortex.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.ThemeDp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.storage.RecyclableStorageCell
import io.github.excu101.vortex.ui.component.themeMeasure
import io.github.excu101.vortex.utils.icon
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin


class StandardStorageLinearCell(context: Context) : ThemeFrameLayout(context),
    RecyclableStorageCell {

    private val largeInnerPadding = 16.dp
    private val middleInnerPadding = 8.dp

    private val titlePadding = ThemeDp(storageListItemHorizontalTitlePaddingKey)
    private val infoPadding = ThemeDp(storageListItemHorizontalSubtitlePaddingKey)

    private val iconSize = 40.dp

    private var selectionFactor = 0F
        set(value) {
            field = value
            invalidate()
        }

    private var selectionBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        color =
            ThemeColor(io.github.excu101.vortex.theme.key.storageListItemIconSelectedTintColorKey)
        style = Paint.Style.FILL
    }

//    private var selectionOuterBackgroundPaint =
//        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
//            color = Color.GRAY
//            style = Paint.Style.FILL
//        }

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

    override var isCellSelected: Boolean = false
        set(value) {
            field = value
            createSelectionAnimator().start()
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

    override fun onDrawForeground(canvas: Canvas?) {
        if (isCellSelected) {
            val radis = toRadians(45.0)
            val centerX =
                ((iconView.left + iconView.right) * 0.5F) + (iconView.width / 2 * sin(radis))
            val centerY =
                ((iconView.top + iconView.bottom) * 0.5F) + (iconView.height / 2 * cos(radis))
            val radius = 9F.dp
//            canvas?.drawArc(
//                (centerX - radius).toFloat(),
//                (centerY - radius).toFloat(),
//                (centerX + radius).toFloat(),
//                (centerY + radius).toFloat(),
//                135F,
//                170F * selectionFactor,
//                false,
//                selectionOuterBackgroundPaint
//            )
            canvas?.drawCircle(
                centerX.toFloat(),
                centerY.toFloat(),
                (radius - 1F.dp) * selectionFactor,
                selectionBackgroundPaint
            )
        }
        super.onDrawForeground(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        themeMeasure(
            widthSpec = widthMeasureSpec,
            heightSpec = heightMeasureSpec,
            widthKey = storageListItemLinearWidthDimenKey,
            heightKey = storageListItemLinearHeightDimenKey,
            ::setMeasuredDimension
        )

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
        if (isCellSelected == isSelected) return
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

    private fun createSelectionAnimator(): ValueAnimator {
        val start = if (isCellSelected) 1F else 0F
        val end = if (isCellSelected) 0F else 1F

        val animator = ValueAnimator.ofFloat(start, end)
        animator.duration = 150L
        animator.interpolator = FastOutSlowInInterpolator()
        animator.addUpdateListener { selectionFactor = it.animatedFraction }
        return animator
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
                intArrayOf(),
            ),
            intArrayOf(
                ThemeColor(storageListItemSurfaceColorKey)
            )
        )
    }

    private fun createRippleStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf() // default
            ),
            intArrayOf(
                ThemeColor(storageListItemSurfaceRippleColorKey) // default
            )
        )
    }

    private fun createTitleStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(),
            ),
            intArrayOf(
                ThemeColor(storageListItemTitleTextColorKey)
            )
        )
    }

    private fun createInfoStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(),
            ),
            intArrayOf(
                ThemeColor(storageListItemSecondaryTextColorKey)
            )
        )
    }

    private fun createIconStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(),
            ),
            intArrayOf(
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
                intArrayOf(),
            ),
            intArrayOf(
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