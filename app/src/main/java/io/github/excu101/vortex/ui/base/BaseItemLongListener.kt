package io.github.excu101.vortex.ui.base

import android.view.View

fun interface BaseItemLongListener<T> {

    fun onClick(view: View, value: T, position: Int): Boolean

}