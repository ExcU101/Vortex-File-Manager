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
import io.github.excu101.manager.ui.theme.FormatterThemeText
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.manager.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.storage.RecyclableStorageCell
import io.github.excu101.vortex.ui.component.theme.key.storageListItemIconBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemIconBackgroundSelectedColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemIconSelectedTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemIconTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemSurfaceColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemSurfaceRippleColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemSurfaceSelectedColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemTitleSelectedTextColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemTitleTextColorKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemNameKey

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
        ColorStateList.valueOf(ThemeColor(storageListItemIconTintColorKey)),
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
        setColorFilter(ThemeColor(storageListItemIconTintColorKey))
    }
    private val titleView = TextView(context).apply {
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textSize = 16F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(ThemeColor(storageListItemTitleTextColorKey))
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
        setTint(ThemeColor(storageListItemSurfaceColorKey))
    }

    private val background = RippleDrawable(
        ColorStateList.valueOf(ThemeColor(storageListItemSurfaceRippleColorKey)),
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
            titleColor = ThemeColor(storageListItemTitleSelectedTextColorKey)
//            infoColor = ThemeColor(storageListItemSecondarySelectedTextColorKey)
            iconView.setColorFilter(ThemeColor(storageListItemIconSelectedTintColorKey))
            iconShape.setTint(ThemeColor(storageListItemIconBackgroundSelectedColorKey))
            shape.setTint(ThemeColor(storageListItemSurfaceSelectedColorKey))
        } else {
            titleColor = ThemeColor(storageListItemTitleTextColorKey)
//            infoColor = ThemeColor(storageListItemSecondaryTextColorKey)
            iconView.setColorFilter(ThemeColor(storageListItemIconTintColorKey))
            iconShape.setTint(ThemeColor(storageListItemIconBackgroundColorKey))
            shape.setTint(ThemeColor(storageListItemSurfaceColorKey))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }

    override fun onBind(item: PathItem) {
//        icon = item.icon
        title = FormatterThemeText(key = fileListItemNameKey, item.name)
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