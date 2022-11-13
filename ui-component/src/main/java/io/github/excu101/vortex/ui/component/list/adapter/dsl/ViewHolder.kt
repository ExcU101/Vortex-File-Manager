package io.github.excu101.vortex.ui.component.list.adapter.dsl

import android.view.View
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

class ViewHolderDslBuilder<T, V : View> {

    var onBind: ((T, V) -> Unit)? = null

    var onUnbind: ((V) -> Unit)? = null

    var onBindSelection: ((Boolean) -> Unit)? = null

    var onBindListener: ((View.OnClickListener) -> Unit)? = null

    var onBindLongListener: ((View.OnLongClickListener) -> Unit)? = null

    fun build(view: V): ViewHolder<T> = object : ViewHolder<T>(view) {
        override fun bind(item: T) {
            onBind?.invoke(item, view)
        }

        override fun unbind() {
            onUnbind?.invoke(view)
        }

        override fun bindSelection(isSelected: Boolean) {
            onBindSelection?.invoke(isSelected)
        }

        override fun bindListener(listener: View.OnClickListener) {
            onBindListener?.invoke(listener)
        }

        override fun bindLongListener(listener: View.OnLongClickListener) {
            onBindLongListener?.invoke(listener)
        }
    }

}

inline fun <T, V : View> viewHolder(
    view: V,
    block: ViewHolderDslBuilder<T, V>.() -> Unit,
): ViewHolder<T> {
    return ViewHolderDslBuilder<T, V>().apply(block).build(view)
}