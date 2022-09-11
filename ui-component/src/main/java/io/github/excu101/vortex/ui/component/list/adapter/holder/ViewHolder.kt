package io.github.excu101.vortex.ui.component.list.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder<T>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: T) // Bind itemView with given item (data)

    abstract fun unbind() // Unbind view, when it's recycled

    open fun bindSelection(isSelected: Boolean) {}

    open fun bindListener(listener: View.OnClickListener) {}

    open fun bindLongListener(listener: View.OnLongClickListener) {}

    open fun bindSelectionListener(listener: View.OnClickListener) {}

    open fun bindSelectionLongListener(listener: View.OnLongClickListener) {}

    open fun unbindListeners() {}

}