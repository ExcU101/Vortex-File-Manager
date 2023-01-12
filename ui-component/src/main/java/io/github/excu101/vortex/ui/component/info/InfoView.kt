package io.github.excu101.vortex.ui.component.info

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.storageListWarningIconTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListWarningTitleTextColorKey

class InfoView(context: Context) : ThemeLinearLayout(context) {

    private val iconView = ImageView(context).apply {
        minimumHeight = 56.dp
        minimumWidth = 56.dp
        setColorFilter(ThemeColor(storageListWarningIconTintColorKey))
    }

    private val messageView = TextView(context).apply {
        textSize = 20F
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        setTextColor(ThemeColor(storageListWarningTitleTextColorKey))
    }

    var icon: Drawable?
        get() = iconView.drawable
        set(value) {
            iconView.setImageDrawable(value)
        }

    var message: CharSequence?
        get() = messageView.text
        set(value) {
            messageView.text = value
        }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        addView(iconView)
        addView(messageView, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    }

    override fun onColorChanged() {
        iconView.setColorFilter(ThemeColor(storageListWarningIconTintColorKey))
        messageView.setTextColor(ThemeColor(storageListWarningTitleTextColorKey))
    }

}