package io.github.excu101.vortex.ui.component.storage.standard.grid

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel
import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.theme.key.*

class StorageGridCell(
    context: Context,
) : ThemeFrameLayout(context), ViewHolder.RecyclableView<PathItem> {

    private val icon = ImageView(context)
    private val title = TextView(context)

    var titleColor: Int
        get() = title.currentTextColor
        set(value) {
            title.setTextColor(value)
        }

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
            icon.setImageDrawable(value)
        }

    var name: CharSequence?
        get() = title.text
        set(value) {
            title.text = value
        }

    init {
        isFocusable = true
        isClickable = true
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

    override fun onBind(item: PathItem) {
        name = FormatterThemeText(
            key = fileListItemNameKey,
            item.name
        )
        icon.setImageResource(item.icon)
    }

    override fun onUnbind() {
        name = null
        image = null
    }

    override fun onBindListener(listener: OnClickListener) {
        setOnIconClickListener(listener)
        setOnClickListener(listener)
    }

}