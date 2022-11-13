package io.github.excu101.vortex.ui.component.list.adapter.listener

import android.view.View

fun interface ItemViewLongListener<T> {

    fun onLongClick(view: View, item: T, position: Int): Boolean

}