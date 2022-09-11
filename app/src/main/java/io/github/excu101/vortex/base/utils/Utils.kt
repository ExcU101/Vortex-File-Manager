package io.github.excu101.vortex.base.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow

fun <T : Any?> T.logIt(tag: String = "Loggable", message: String = "", type: Int = Log.VERBOSE): T {
    Log.println(type, tag, message + this.toString())
    return this
}

fun <T : Any> T.toastIt(
    context: Context,
    message: String = "",
    duration: Int = Toast.LENGTH_SHORT,
): T {
    Toast.makeText(context, message + this.toString(), duration).show()
    return this
}

fun <T : Any> T.snackIt(
    view: View,
    message: String = "",
    duration: Int = Snackbar.LENGTH_SHORT,
    anchorView: View? = null,
    animationMode: Int = BaseTransientBottomBar.ANIMATION_MODE_SLIDE,
): T {
    Snackbar.make(view, message + this.toString(), duration)
        .setAnimationMode(animationMode)
        .setAnchorView(anchorView)
        .show()
    return this
}

suspend inline fun <T> MutableStateFlow<T>.applyValue(block: T.() -> Unit) {
    emit(value.apply(block))
}

suspend inline fun <T> MutableStateFlow<T>.new(block: T.() -> T) {
    emit(block(value))
}