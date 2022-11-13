package io.github.excu101.vortex.ui.component.drawer

import android.content.Context
import android.content.res.Configuration.*
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.R.style.TextAppearance_AppCompat_Headline
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.NavigationDrawerHeaderItem
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

class NavigationDrawerHeaderItemView(
    context: Context,
) : ThemeLinearLayout(
    context
), ViewHolder.RecyclableView<NavigationDrawerHeaderItem> {

    private val logo: ImageView = ImageView(context).apply {
        setImageResource(R.mipmap.ic_launcher_round)
    }

    private val title = TextView(context).apply {
        text = "Vortex File Manager"
        setTextAppearance(TextAppearance_AppCompat_Headline)
    }

    private val switcher = ImageView(context).apply {
        val isDark = when (resources.configuration.uiMode and UI_MODE_NIGHT_MASK) {
            UI_MODE_NIGHT_YES -> false
            UI_MODE_NIGHT_NO -> true
            else -> true
        }
        setImageResource(if (isDark) R.drawable.ic_light_mode_24 else R.drawable.ic_dark_mode_24)
    }

    init {
        gravity = Gravity.CENTER_VERTICAL
        orientation = HORIZONTAL
        minimumHeight = 56.dp

        addView(logo, LayoutParams(48.dp, 48.dp))
        addView(title)
        addView(switcher)
    }

    override fun onChanged() {

    }

    override fun onBind(item: NavigationDrawerHeaderItem) {

    }

    override fun onUnbind() {

    }
}