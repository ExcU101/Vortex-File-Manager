package io.github.excu101.vortex.ui.component.adapter.selection

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.adapter.listener.ClickListenerRegister
import io.github.excu101.vortex.ui.component.adapter.section.SelectionOwner

abstract class SelectionListAdapter<T, VH : RecyclerView.ViewHolder>(
    differ: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, VH>(differ), ClickListenerRegister<T>, SelectionOwner<T> {

    abstract val selectedCount: Int

    abstract fun isSelected(position: Int): Boolean

    fun item(position: Int): T = getItem(position)

    fun position(item: T): Int = currentList.indexOf(item)

}