package io.github.excu101.vortex.base.utils

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun <T : Any?> T.logIt(tag: String = "Loggable", type: Int = Log.VERBOSE): T {
    Log.println(type, tag, this.toString())
    return this
}

suspend fun <T> MutableStateFlow<T>.applyValue(block: T.() -> Unit) {
    emit(value.apply(block))
}

suspend fun <T> MutableStateFlow<T>.new(block: T.() -> T) {
    emit(block(value))
}