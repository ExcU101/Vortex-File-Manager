package io.github.excu101.vortex.ui.component.header

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.updatePadding
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.fileItemTitleTextColorKey

class TextHeaderView(
    context: Context,
) : FrameLayout(context) {

    private val title = TextView(context).apply {
        setTextColor(ThemeColor(fileItemTitleTextColorKey))
    }

    init {
        updatePadding(16.dp, top = 16.dp, bottom = 16.dp)
        addView(title)
    }

    fun setTitle(value: String? = null) {
        title.text = value
    }

}