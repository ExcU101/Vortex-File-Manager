package io.github.excu101.vortex.utils

inline fun Boolean.onTrue(block: () -> Unit) {
    if (this) block()
}

inline fun Boolean.onFalse(block: () -> Unit) {
    if (!this) block()
}