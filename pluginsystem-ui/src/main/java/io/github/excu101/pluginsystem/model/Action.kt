package io.github.excu101.pluginsystem.model

import android.graphics.drawable.Drawable

interface Action {
    val title: String
    val icon: Drawable?
}

private data class ActionImpl(
    override val title: String,
    override val icon: Drawable?,
) : Action {

    override fun toString(): String {
        var result = ""
        result += "Title: $title\n"
        result += "Icon: $icon\n"
        return result

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActionImpl

        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }
}

fun action(
    title: String,
    icon: Drawable?,
): Action = ActionImpl(title, icon)