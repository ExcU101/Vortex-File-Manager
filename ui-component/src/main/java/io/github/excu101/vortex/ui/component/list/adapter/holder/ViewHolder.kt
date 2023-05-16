package io.github.excu101.vortex.ui.component.list.adapter.holder

import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder

open class ViewHolder<T>(itemView: View) : ViewHolder(itemView) {

    @Suppress("UNCHECKED_CAST")
    private val delegate = itemView as? RecyclableView<T>

    // Bind itemView with given item (data)
    open fun bind(item: T) {
        delegate?.onBind(item)
    }

    open fun bindPayload(item: Any?) {
        delegate?.onBindPayload(item)
    }

    // Unbind view, when it's recycled
    open fun unbind() {
        delegate?.onUnbind()
    }

    open fun bindSelection(isSelected: Boolean) {
        delegate?.onBindSelection(isSelected)
    }

    open fun bindListener(listener: OnClickListener) {
        delegate?.onBindListener(listener)
    }

    open fun bindLongListener(listener: OnLongClickListener) {
        delegate?.onBindLongListener(listener)
    }

    open fun bindSelectionListener(listener: OnClickListener) {}

    open fun bindSelectionLongListener(listener: OnLongClickListener) {}

    open fun unbindListeners() {
        delegate?.onUnbindListeners()
    }

    interface RecyclableView<T> {
        fun onBind(item: T)

        fun onUnbind()

        fun onBindPayload(payload: Any?) {

        }

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