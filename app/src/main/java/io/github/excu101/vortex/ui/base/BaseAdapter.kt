package io.github.excu101.vortex.ui.base

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<T, VH : BaseViewHolder<T>>(
    differ: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, VH>(differ) {

    private val listeners: MutableList<BaseItemListener<T>> = arrayListOf()
    private val longListeners: MutableList<BaseItemLongListener<T>> = arrayListOf()

    fun register(listener: BaseItemListener<T>) {
        listeners.add(listener)
    }

    fun unregister(listener: BaseItemListener<T>) {
        listeners.remove(listener)
    }

    fun registerLong(listener: BaseItemLongListener<T>) {
        longListeners.add(listener)
    }

    fun unregisterLong(listener: BaseItemLongListener<T>) {
        longListeners.remove(listener)
    }

    protected fun notify(view: View, value: T, position: Int) {
        listeners.forEach { listener ->
            listener.onClick(view, value, position)
        }
    }

    protected fun notifyLong(view: View, value: T, position: Int): Boolean {
        var result = false
        longListeners.forEach { listener ->
            result = listener.onClick(view, value, position)
        }
        return result
    }

    fun addItem(value: T) {
        addItem(position = itemCount, value)
    }

    fun addItem(position: Int, value: T) {
        currentList.add(position, value)
        notifyItemInserted(position)
    }

    fun removeItem(value: T) {
        removeItem(position = indexOf(value))
    }

    fun removeItem(position: Int) {
        currentList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun indexOf(value: T): Int {
        return currentList.indexOf(value)
    }

}