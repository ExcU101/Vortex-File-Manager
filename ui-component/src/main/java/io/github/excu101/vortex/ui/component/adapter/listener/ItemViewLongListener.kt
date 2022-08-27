package io.github.excu101.vortex.ui.component.adapter.listener

import android.view.View

fun interface ItemViewLongListener<T> {

    fun onClick(view: View, value: T, position: Int): Boolean

}