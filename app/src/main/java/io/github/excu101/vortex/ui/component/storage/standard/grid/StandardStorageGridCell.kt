package io.github.excu101.vortex.ui.component.storage.standard.grid

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.Gravity.CENTER
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.contains
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.theme.key.storageListItemIconBackgroundColorKey
import io.github.excu101.vortex.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.storage.RecyclableStorageCell

class StandardStorageGridCell(
    context: Context,
) : ThemeLinearLayout(context), RecyclableStorageCell {

    private val iconSize = 40.dp

    override var info: String? = null

    private val iconShape = MaterialShapeDrawable(
        ShapeAppearanceModel.Builder().setAllCorners(CornerFamily.ROUNDED, 500F).build()
    ).apply {
        setTint(ThemeColor(storageListItemIconBackgroundColorKey))
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

    private val iconBackground = RippleDrawable(
        ColorStateList.valueOf(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemIconTintColorKey)),
        iconShape,
        null
    )

    private val iconView = object : AppCompatImageView(context) {
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
        }
    }.apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        background = iconBackground
        minimumWidth = iconSize
        minimumHeight = iconSize
        setColorFilter(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemIconTintColorKey))
    }
    private val titleView = TextView(context).apply {
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textSize = 16F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemTitleTextColorKey))
    }

    var titleColor: Int
        get() = titleView.currentTextColor
        set(value) {
            titleView.setTextColor(value)
        }

    private val shape = MaterialShapeDrawable(
        builder().setAllCorners(CornerFamily.ROUNDED, 16F.dp).build()
    ).apply {
        shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
        initializeElevationOverlay(context)
        setUseTintColorForShadow(true)
        setTint(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemSurfaceColorKey))
    }

    private val background = RippleDrawable(
        ColorStateList.valueOf(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemSurfaceRippleColorKey)),
        shape,
        null
    )

    override var icon: Drawable?
        get() = iconView.drawable
        set(value) {
            ensureContainingIcon()
            iconView.setImageDrawable(value)
        }

    override var title: String?
        get() = titleView.text.toString()
        set(value) {
            ensureContainingTitle()
            titleView.text = value
        }

    private val containsIcon
        get() = contains(iconView)

    private val containsTitle
        get() = contains(titleView)

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(titleView)
        }
    }

    private fun ensureContainingIcon() {
        if (!containsIcon) {
            addView(iconView)
        }
    }

    init {
        id = ViewIds.Storage.Item.RootId
        iconView.id = ViewIds.Storage.Item.IconId

        isFocusable = true
        isClickable = true
        orientation = VERTICAL
        gravity = CENTER
        setBackground(background)
    }

    private var isItemSelected = false

    fun updateSelection(isSelected: Boolean) {
        isItemSelected = isSelected
        updateSelectionState()
    }

    fun setOnIconClickListener(listener: OnClickListener?) {
        iconView.setOnClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: OnLongClickListener?) {
        iconView.setOnLongClickListener(listener)
    }


    private fun updateSelectionState() {
        if (isItemSelected) {
            titleColor =
                ThemeColor(io.github.excu101.vortex.theme.key.storageListItemTitleSelectedTextColorKey)
//            infoColor = ThemeColor(storageListItemSecondarySelectedTextColorKey)
            iconView.setColorFilter(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemIconSelectedTintColorKey))
            iconShape.setTint(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemIconBackgroundSelectedColorKey))
            shape.setTint(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemSurfaceSelectedColorKey))
        } else {
            titleColor =
                ThemeColor(io.github.excu101.vortex.theme.key.storageListItemTitleTextColorKey)
//            infoColor = ThemeColor(storageListItemSecondaryTextColorKey)
            iconView.setColorFilter(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemIconTintColorKey))
            iconShape.setTint(ThemeColor(storageListItemIconBackgroundColorKey))
            shape.setTint(ThemeColor(io.github.excu101.vortex.theme.key.storageListItemSurfaceColorKey))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }

    override fun onBind(item: PathItem) {
//        icon = item.icon
        title = io.github.excu101.vortex.theme.FormatterThemeText(
            key = io.github.excu101.vortex.theme.key.text.storage.item.fileListItemNameKey,
            item.name
        )
        info = item.info
    }

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
        shape.elevation = elevation
    }

    override fun onColorChanged() {

    }

    override fun onBindListener(listener: OnClickListener) {
        setOnIconClickListener(listener)
        setOnClickListener(listener)
    }

    override fun onBindLongListener(listener: OnLongClickListener) {
        setOnIconLongClickListener(listener)
        setOnLongClickListener(listener)
    }

    override fun onBindSelection(isSelected: Boolean) {
        updateSelection(isSelected = isSelected)
    }

    override fun onUnbindListeners() {
        setOnClickListener(null)
        setOnIconClickListener(null)
        setOnLongClickListener(null)
        setOnIconLongClickListener(null)
    }

}