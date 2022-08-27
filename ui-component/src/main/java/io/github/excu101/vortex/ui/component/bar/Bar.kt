package io.github.excu101.vortex.ui.component.bar

import android.content.Context
import android.widget.FrameLayout
import io.github.excu101.pluginsystem.ui.theme.ThemeDimen
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.mainBarHeightKey

class Bar(context: Context) : FrameLayout(context) {

    enum class Type {
        TOP,
        BOTTOM
    }

    val type: Type
        get() = Type.BOTTOM


    init {
        minimumHeight = ThemeDimen(mainBarHeightKey).dp
    }
}