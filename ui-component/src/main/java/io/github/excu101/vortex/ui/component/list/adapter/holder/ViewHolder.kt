package io.github.excu101.vortex.ui.component.list.adapter.holder

import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder

open class ViewHolder<T>(itemView: View) : ViewHolder(itemView) {

    // Bind itemView with given item (data)
    @Suppress("UNCHECKED_CAST")
    open fun bind(item: T) {
        (itemView as? RecyclableView<T>)?.onBind(item)
    }

    // Unbind view, when it's recycled
    @Suppress("UNCHECKED_CAST")
    open fun unbind() {
        (itemView as? RecyclableView<T>)?.onUnbind()
    }

    @Suppress("UNCHECKED_CAST")
    open fun bindSelection(isSelected: Boolean) {
        (itemView as? RecyclableView<T>)?.onBindSelection(isSelected)
    }

    @Suppress("UNCHECKED_CAST")
    open fun bindListener(listener: OnClickListener) {
        (itemView as? RecyclableView<T>)?.onBindListener(listener)
    }

    @Suppress("UNCHECKED_CAST")
    open fun bindLongListener(listener: OnLongClickListener) {
        (itemView as? RecyclableView<T>)?.onBindLongListener(listener)
    }

    open fun bindSelectionListener(listener: OnClickListener) {}

    open fun bindSelectionLongListener(listener: OnLongClickListener) {}

    @Suppress("UNCHECKED_CAST")
    open fun unbindListeners() {
        (itemView as? RecyclableView<T>)?.onUnbindListeners()
    }

    interface RecyclableView<T> {
        fun onBind(item: T)

        fun onUnbind()

        fun onBindSelection(isSelected: Boolean) {

        }

        fun onBindListener(listener: OnClickListener) {

        }

        fun onBindLongListener(listener: OnLongClickListener) {

        }

        fun onUnbindListeners() {

        }
    }

}