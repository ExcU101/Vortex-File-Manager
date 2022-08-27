package io.github.excu101.vortex.ui.component.adapter.selection

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.adapter.listener.ClickListenerRegister

abstract class SelectionListAdapter<T, VH : RecyclerView.ViewHolder>(
    differ: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, VH>(differ), ClickListenerRegister<T> {

    abstract fun isSelected(position: Int): Boolean

}