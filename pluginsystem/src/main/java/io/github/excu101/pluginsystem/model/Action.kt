package io.github.excu101.pluginsystem.model

import android.graphics.drawable.Drawable

interface Action {
    val title: String
    val icon: Drawable
}

private data class ActionImpl(
    override val title: String,
    override val icon: Drawable,
) : Action {
    override fun hashCode(): Int = title.hashCode()
}

fun action(
    title: String,
    icon: Drawable,
): Action = ActionImpl(title, icon)