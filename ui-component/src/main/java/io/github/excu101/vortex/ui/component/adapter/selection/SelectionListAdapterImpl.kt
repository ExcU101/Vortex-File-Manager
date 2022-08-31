package io.github.excu101.vortex.ui.component.adapter.selection

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.adapter.listener.ItemViewLongListener
import io.github.excu101.vortex.ui.component.adapter.listener.ItemViewSelectionListener
import io.github.excu101.vortex.ui.component.adapter.listener.SelectionListenerRegister
import io.github.excu101.vortex.ui.component.adapter.section.SelectionOwner
import io.github.excu101.vortex.ui.component.adapter.section.SelectionOwner.Mode.MULTIPLE

abstract class SelectionListAdapterImpl<T, VH : RecyclerView.ViewHolder>(
    differ: DiffUtil.ItemCallback<T>,
) : SelectionListAdapter<T, VH>(differ), SelectionListenerRegister<T> {

    private val listeners = mutableListOf<ItemViewListener<T>>()
    private val longListeners = mutableListOf<ItemViewLongListener<T>>()
    private val selectionListeners = mutableListOf<ItemViewSelectionListener<T>>()

    override var selectionMode: SelectionOwner.Mode = MULTIPLE

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
            clicked = listener.onClick(view, item, position)
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
        var clicked = false

        selectionListeners.forEach { listener ->
            clicked = listener.onSelectionChanged(view, item, position, isSelected)
        }

        return clicked
    }


}