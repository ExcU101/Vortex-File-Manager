package io.github.excu101.vortex.ui.component.menu

import android.graphics.drawable.Drawable

interface MenuAction {
    val id: Int
    val title: String
    val icon: Drawable?
}

fun MenuAction(
    id: Int,
    title: String,
    icon: Drawable?
): MenuAction {
    return object : MenuAction {
        override val id: Int = id
        override val title: String = title
        override val icon: Drawable? = icon
    }
}