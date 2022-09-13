package io.github.excu101.vortex.ui.component.header.text

import android.content.Context
import android.widget.TextView
import androidx.core.view.updatePadding
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.fileItemTitleTextColorKey
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerTitleColorKey

class TextHeaderView(
    context: Context,
) : ThemeFrameLayout(context) {

    private val desireHeight = 48.dp

    private val title = TextView(context).apply {
        setTextColor(ThemeColor(mainDrawerTitleColorKey))
        textSize = 14F
    }

    init {
        updatePadding(16.dp, top = 16.dp, bottom = 16.dp)
        setBackgroundColor(ThemeColor(mainDrawerBackgroundColorKey))
        addView(title)
    }

    fun setTitle(value: String? = null) {
        title.text = value
    }

    override fun onChanged() {
        title.setTextColor(ThemeColor(mainDrawerTitleColorKey))
        setBackgroundColor(ThemeColor(mainDrawerBackgroundColorKey))
    }

}