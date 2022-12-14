package io.github.excu101.vortex.ui.component.storage.standard.grid

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.Gravity.CENTER
import android.widget.FrameLayout.*
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.contains
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel
import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemNameKey

class StandardStorageGridCell(
    context: Context,
) : ThemeLinearLayout(context), ViewHolder.RecyclableView<PathItem> {

    private val iconSize = 40.dp

    private val iconShape = MaterialShapeDrawable(
        ShapeAppearanceModel.Builder().setAllCorners(CornerFamily.ROUNDED, 500F).build()
    ).apply {
        setTint(ThemeColor(storageListItemIconBackgroundColorKey))
    }

    private val iconBackground = RippleDrawable(
        ColorStateList.valueOf(ThemeColor(storageListItemIconTintColorKey)),
        iconShape,
        null
    )

    private val icon = ImageView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        background = iconBackground
        minimumWidth = iconSize
        minimumHeight = iconSize
        setColorFilter(ThemeColor(storageListItemIconTintColorKey))
    }
    private val title = TextView(context).apply {
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textSize = 16F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(ThemeColor(storageListItemTitleTextColorKey))
    }

    var titleColor: Int
        get() = title.currentTextColor
        set(value) {
            title.setTextColor(value)
        }

    private val shape = MaterialShapeDrawable(

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

    var image: Drawable?
        get() = icon.drawable
        set(value) {
            ensureContainingIcon()
            icon.setImageDrawable(value)
        }

    var name: CharSequence?
        get() = title.text
        set(value) {
            ensureContainingTitle()
            title.text = value
        }

    private val containsIcon
        get() = contains(icon)

    private val containsTitle
        get() = contains(title)

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(title)
        }
    }

    private fun ensureContainingIcon() {
        if (!containsIcon) {
            addView(icon)
        }
    }

    init {
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
        icon.setOnClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: OnLongClickListener?) {
        icon.setOnLongClickListener(listener)
    }


    private fun updateSelectionState() {
        if (isItemSelected) {
            titleColor = ThemeColor(storageListItemTitleSelectedTextColorKey)
//            infoColor = ThemeColor(storageListItemSecondarySelectedTextColorKey)
            icon.setColorFilter(ThemeColor(storageListItemIconSelectedTintColorKey))
            iconShape.setTint(ThemeColor(storageListItemIconBackgroundSelectedColorKey))
            shape.setTint(ThemeColor(storageListItemSurfaceSelectedColorKey))
        } else {
            titleColor = ThemeColor(storageListItemTitleTextColorKey)
//            infoColor = ThemeColor(storageListItemSecondaryTextColorKey)
            icon.setColorFilter(ThemeColor(storageListItemIconTintColorKey))
            iconShape.setTint(ThemeColor(storageListItemIconBackgroundColorKey))
            shape.setTint(ThemeColor(storageListItemSurfaceColorKey))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
        shape.elevation = elevation
    }

    override fun onChanged() {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBind(item: PathItem) {
        image = item.icon
        name = FormatterThemeText(
            key = fileListItemNameKey,
            item.name
        )
    }

    override fun onUnbind() {
        name = null
        image = null
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