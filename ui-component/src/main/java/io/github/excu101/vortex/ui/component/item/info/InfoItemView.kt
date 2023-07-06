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
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.theme.key.mainBarSubtitleTextColorKey
import io.github.excu101.vortex.theme.key.mainBarTitleTextColorKey
import io.github.excu101.vortex.theme.key.mainDrawerBackgroundColorKey
import io.github.excu101.vortex.theme.key.storageListItemSurfaceRippleColorKey

class InfoItemView(
    context: Context,
) : io.github.excu101.vortex.theme.widget.ThemeLinearLayout(
    context
), ViewHolder.RecyclableView<InfoItem> {

    private val shape = MaterialShapeDrawable().apply {
        setTint(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainDrawerBackgroundColorKey))
    }

    private val background = RippleDrawable(
        ColorStateList.valueOf(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.storageListItemSurfaceRippleColorKey)),
        shape,
        null
    )

    private val titleView = TextView(context).apply {
        setTextColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainBarTitleTextColorKey))
        textSize = 14F
    }

    private val descriptionView = TextView(context).apply {
        setTextColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainBarSubtitleTextColorKey))
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

    override fun onBindListener(listener: OnClickListener) {
        setOnClickListener(listener)
    }

    override fun onUnbindListeners() {
        setOnClickListener(null)
    }

    override fun onColorChanged() {
        titleView.setTextColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainBarTitleTextColorKey))
        descriptionView.setTextColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainBarSubtitleTextColorKey))
        shape.setTint(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainDrawerBackgroundColorKey))
    }


}