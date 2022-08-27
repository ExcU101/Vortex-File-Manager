package io.github.excu101.vortex.ui.component.adapter.selection

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.adapter.listener.ItemViewLongListener

abstract class SelectionAdapterImpl<T, VH : RecyclerView.ViewHolder>(protected val differ: DiffUtil.ItemCallback<T>? = null) :
    SelectionAdapter<T, VH>() {

    private val listeners = mutableListOf<ItemViewListener<T>>()
    private val longListeners = mutableListOf<ItemViewLongListener<T>>()

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
        adapterList.clear()
        adapterList.addAll(items)
        differ?.let { DiffUtil.calculateDiff(it).dispatchUpdatesTo(this) } ?: notifyDataSetChanged()
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