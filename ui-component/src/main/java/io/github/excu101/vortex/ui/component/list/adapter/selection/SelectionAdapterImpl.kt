package io.github.excu101.vortex.ui.component.list.adapter.selection

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewSelectionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.withContext

abstract class SelectionAdapterImpl<T, VH : RecyclerView.ViewHolder>(protected val differ: DiffUtil.ItemCallback<T>? = null) :
    SelectionAdapter<T, VH>() {

    private val selectionListeners = mutableListOf<ItemViewSelectionListener<T>>()
    private val listeners = mutableListOf<ItemViewListener<T>>()
    private val longListeners = mutableListOf<ItemViewLongListener<T>>()

    @OptIn(ObsoleteCoroutinesApi::class)
    private val refresher =
        CoroutineScope(Default).actor<Pair<DiffUtil.Callback, List<T>>>(capacity = CONFLATED) {
            for ((differ, items) in channel) {
                val result = DiffUtil.calculateDiff(differ)
                withContext(SupervisorJob() + Main) {
                    adapterList.clear()
                    adapterList.addAll(items)
                    result.dispatchUpdatesTo(this@SelectionAdapterImpl)
                }
            }
        }

    protected val adapterList: MutableList<T> = mutableListOf()
    val list: List<T>
        get() = adapterList

    override fun add(item: T) = add(adapterList.size, item)

    override fun add(position: Int, item: T) {
        adapterList.add(position, item)
        notifyItemInserted(position)
    }

    override fun add(items: Iterable<T>) {
        val oldSize = list.size
        adapterList.addAll(items)
        notifyItemRangeInserted(oldSize, itemCount)
    }

    override fun remove(item: T) = remove(position(item))

    override fun remove(position: Int) {
        adapterList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun replace(
        items: List<T>,
        differ: DiffUtil.Callback?,
    ) {
        if (differ == null) {
            adapterList.clear()
            adapterList.addAll(items)
            notifyDataSetChanged()
        } else {
            refresher.trySend(element = differ to items)
        }
    }

    override fun item(position: Int): T = adapterList[position]

    override fun position(item: T): Int = adapterList.indexOf(item)

    override fun getItemCount(): Int = adapterList.size

    override fun register(listener: ItemViewListener<T>) {
        listeners.add(listener)
    }

    override fun registerLong(listener: ItemViewLongListener<T>) {
        longListeners.add(listener)
    }

    protected fun notify(view: View, item: T, position: Int) {
        listeners.forEach { listener ->
            listener.onClick(view, item, position)
        }
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