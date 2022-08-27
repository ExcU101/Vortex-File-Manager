package io.github.excu101.vortex.ui.component.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder<T>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: T)

    abstract fun unbind()

    open fun bindListener(listener: View.OnClickListener) {}

    open fun bindLongListener(listener: View.OnLongClickListener) {}

    open fun unbindListeners() {}

}