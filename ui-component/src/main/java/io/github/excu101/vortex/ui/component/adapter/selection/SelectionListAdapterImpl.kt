package io.github.excu101.vortex.ui.component.adapter.selection

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.adapter.listener.ItemViewLongListener

abstract class SelectionListAdapterImpl<T, VH : RecyclerView.ViewHolder>(
    differ: DiffUtil.ItemCallback<T>,
) : SelectionListAdapter<T, VH>(differ) {

    private val listeners = mutableListOf<ItemViewListener<T>>()
    private val longListeners = mutableListOf<ItemViewLongListener<T>>()

    fun item(position: Int): T = getItem(position)

    fun position(item: T): Int = currentList.indexOf(item)

    override fun register(listener: ItemViewListener<T>) {
        listeners.add(listener)
    }

    override fun registerLong(listener: ItemViewLongListener<T>) {
        longListeners.add(listener)
    }

    protected fun notify(view: View, value: T, position: Int) {
        listeners.forEach { listener ->
            listener.onClick(view, value, position)
        }
    }

    protected fun notifyLong(view: View, value: T, position: Int): Boolean {
        var clicked = false

        longListeners.forEach { listener ->
            clicked = listener.onClick(view, value, position)
        }

        return clicked
    }


}