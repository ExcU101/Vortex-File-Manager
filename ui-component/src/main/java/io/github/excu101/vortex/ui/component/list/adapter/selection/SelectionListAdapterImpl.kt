package io.github.excu101.vortex.ui.component.list.adapter.selection

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewSelectionListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.SelectionListenerRegister

abstract class SelectionListAdapterImpl<T, VH : RecyclerView.ViewHolder>(
    differ: DiffUtil.ItemCallback<T>,
) : SelectionListAdapter<T, VH>(differ), SelectionListenerRegister<T> {

    private val listeners = mutableListOf<ItemViewListener<T>>()
    private val longListeners = mutableListOf<ItemViewLongListener<T>>()
    private val selectionListeners = mutableListOf<ItemViewSelectionListener<T>>()

    override fun register(listener: ItemViewListener<T>) {
        listeners.add(listener)
    }

    override fun registerLong(listener: ItemViewLongListener<T>) {
        longListeners.add(listener)
    }

    override fun registerSelection(listener: ItemViewSelectionListener<T>) {
        selectionListeners.add(listener)
    }

    protected fun notify(view: View, item: T, position: Int) = listeners.forEach { listener ->
        listener.onClick(view, item, position)
    }

    protected fun notifyLong(view: View, item: T, position: Int): Boolean {
        var clicked = false

        longListeners.forEach { listener ->
            clicked = listener.onLongClick(view, item, position)
        }

        return clicked
    }

    protected fun notifySelection(
        view: View,
        item: T,
        position: Int,
        isSelected: Boolean,
    ) = selectionListeners.forEach { listener ->
        listener.onSelectionChanged(view, item, position, isSelected)
    }

    protected fun notifyLongSelection(
        view: View,
        item: T,
        position: Int,
        isSelected: Boolean,
    ): Boolean {
        selectionListeners.forEach { listener ->
            if (listener.onSelectionChanged(view, item, position, isSelected)) return true
        }

        return false
    }


}