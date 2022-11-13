package io.github.excu101.vortex.ui.component.item.info

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.contains
import androidx.core.view.updatePadding
import com.google.android.material.shape.MaterialShapeDrawable
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.theme.key.mainBarSubtitleTextColorKey
import io.github.excu101.vortex.ui.component.theme.key.mainBarTitleTextColorKey
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemSurfaceRippleColorKey

class InfoItemView(
    context: Context,
) : ThemeLinearLayout(
    context
), ViewHolder.RecyclableView<InfoItem> {

    private val shape = MaterialShapeDrawable().apply {
        setTint(ThemeColor(mainDrawerBackgroundColorKey))
    }

    private val background = RippleDrawable(
        ColorStateList.valueOf(ThemeColor(storageListItemSurfaceRippleColorKey)),
        shape,
        null
    )

    private val titleView = TextView(context).apply {
        setTextColor(ThemeColor(mainBarTitleTextColorKey))
        textSize = 14F
    }

    private val descriptionView = TextView(context).apply {
        setTextColor(ThemeColor(mainBarSubtitleTextColorKey))
        textSize = 12F
    }

    private fun ensureContainingTitle() {
        if (!contains(titleView)) {
            addView(titleView)
        }
    }

    private fun ensureContainingDescription() {
        if (!contains(descriptionView)) {
            addView(descriptionView)
        }
    }

    var title: String?
        get() = titleView.text.toString()
        set(value) {
            ensureContainingTitle()
            titleView.text = value
        }

    var description: String?
        get() = descriptionView.text.toString()
        set(value) {
            ensureContainingDescription()
            descriptionView.text = value
        }

    init {
        orientation = VERTICAL
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        updatePadding(
            left = 16.dp,
            right = 16.dp,
            top = 8.dp,
            bottom = 8.dp
        )
        isClickable = true
        isFocusable = true
        setBackground(background)
    }

    override fun onBind(item: InfoItem) {
        title = item.value.value
        description = item.value.description
    }

    override fun onUnbind() {
        title = null
        description = null
    }

    override fun onChanged() {
        titleView.setTextColor(ThemeColor(mainBarTitleTextColorKey))
        descriptionView.setTextColor(ThemeColor(mainBarSubtitleTextColorKey))
        shape.setTint(ThemeColor(mainDrawerBackgroundColorKey))
    }

}