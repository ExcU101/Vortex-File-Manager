package io.github.excu101.vortex.ui.component.list.adapter.listener

import android.view.View

fun interface ItemViewSelectionListener<T> {

    fun onSelectionChanged(view: View, item: T, position: Int, isSelected: Boolean): Boolean

}