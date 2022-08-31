package io.github.excu101.vortex.ui.component.adapter.listener

import android.view.View

fun interface ItemViewSelectionListener<T> {

    fun onSelectionChanged(view: View, item: T, position: Int, isSelected: Boolean): Boolean

}