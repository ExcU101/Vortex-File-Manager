package io.github.excu101.pluginsystem.model

import android.graphics.drawable.Drawable

data class GroupAction(
    val name: String,
    val icon: Drawable? = null,
    val actions: Collection<Action> = listOf(),
)