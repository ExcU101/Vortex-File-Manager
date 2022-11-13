package io.github.excu101.pluginsystem.model

import android.graphics.drawable.Drawable

interface Effect : Action {
    operator fun invoke()
}

private data class EffectImpl(
    override val title: String,
    override val icon: Drawable?,
    val onInvoke: () -> Unit,
) : Effect {
    override operator fun invoke() {
        onInvoke.invoke()
    }
}

fun effect(
    title: String,
    icon: Drawable?,
    onInvoke: () -> Unit,
): Effect = EffectImpl(
    title,
    icon,
    onInvoke = onInvoke
)