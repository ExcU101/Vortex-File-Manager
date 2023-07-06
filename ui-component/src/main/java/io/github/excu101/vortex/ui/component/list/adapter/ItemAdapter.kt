package io.github.excu101.vortex.ui.component.list.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.collection.SparseArrayCompat
import androidx.collection.set
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.Callback
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import io.github.excu101.vortex.ui.component.list.adapter.diff.ItemDiffer
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.listener.ClickListenerRegister
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewSelectionListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.SelectionListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.SelectionListenerRegister
import io.github.excu101.vortex.ui.component.list.adapter.selection.SelectableAdapter
import io.github.excu101.vortex.ui.component.list.adapter.selection.SelectionAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlin.reflect.KClass

class UnsupportedViewTypeException(
    viewType: Int,
) : Throwable("Unsupported view type (viewType: $viewType)")

open class ItemAdapter<T : SuperItem> : SelectionAdapter<T, ViewHolder<T>>, EditableAdapter<T>,
    SelectableAdapter<T>,
    ClickListenerRegister<T>, SelectionListenerRegister<T> {

    companion object {
        const val selectionPayload = "SELECTION"
    }

    init {
        setHasStableIds(true)
    }

    protected var isPayload = true
    protected val selection = mutableSetOf<T>()

    // key: viewType, value: viewHolder factory
    private val factories = SparseArrayCompat<ViewHolderFactory<T>>()

    val viewTypes: Int
        get() = factories.size()

    private val itemViewListeners = mutableListOf<ItemViewListener<T>>()
    private val itemViewLongListeners = mutableListOf<ItemViewLongListener<T>>()
    private val itemViewSelectionListeners = mutableListOf<ItemViewSelectionListener<T>>()
    private val selectionListeners = mutableListOf<SelectionListener<T>>()

    protected val adapterList = mutableListOf<T>()
    val list: List<T>
        get() = adapterList

    private val scope = CoroutineScope(Dispatchers.Default)

    constructor(vararg types: Pair<Int, ViewHolderFactory<T>>) {
        types.forEach { (type, factory) ->
            factories.put(type, factory)
        }
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private val refresher = scope.actor<Pair<Callback, List<T>>>(capacity = Channel.CONFLATED) {
        consumeEach { (differ, items) ->
            val result = calculateDiff(differ)
            withContext(SupervisorJob() + Dispatchers.Main) {
                adapterList.clear()
                adapterList.addAll(items)
                result.dispatchUpdatesTo(this@ItemAdapter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return factories[viewType]?.let { factory ->
            factory.produceViewHolder(factory.produceView(parent))
        } ?: throw UnsupportedViewTypeException(viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val item = item(position)
        val isSelected = isSelected(item)
        holder.bind(item)
        holder.bindSelection(isSelected)
        holder.bindListener { view ->
            notify(view, item, holder.absoluteAdapterPosition)
        }
        holder.bindLongListener { view ->
            notifyLong(view, item, holder.absoluteAdapterPosition)
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder<T>,
        position: Int,
        payloads: List<Any>,
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach { payload ->
                if (payload == selectionPayload) {
                    val isSelected = isSelected(position)
                    holder.bindSelection(isSelected)
                } else {
                    holder.bindPayload(payload)
                }
            }
        }
    }

    override fun select(item: T) {
        if (isSelected(item)) {
            selection.remove(item)
            notifySelection(item, false)
        } else {
            selection.add(item)
            notifySelection(item, true)
        }

        if (isPayload) changed(item, selectionPayload)
        else changed(item)

        notifySelection()
    }

    override fun replaceSelected(selected: List<T>) {

        if (selected == selection) return
        if (selected.isEmpty()) {
            selection.removeAll { item ->
                if (isPayload) changed(item, selectionPayload)
                else changed(item)
                true
            }
        }

        selection.removeAll { item ->
            if (item !in selected) {
                if (isPayload) changed(item, selectionPayload)
                else changed(item)
                notifySelection(item, false)
                true
            } else false
        }

        for (newItem in selected) {
            if (!selected.contains(newItem)) {
                selection.add(newItem)
                if (isPayload) changed(newItem, selectionPayload)
                else changed(newItem)
                notifySelection(newItem, true)
            }
        }

        notifySelection()
    }

    override fun isSelected(position: Int): Boolean {
        return isSelected(item = item(position))
    }

    open fun isSelected(item: T): Boolean {
        return selection.contains(item)
    }

    override fun contains(item: T): Boolean {
        return list.contains(item)
    }

    override fun getItemId(position: Int): Long = item(position).id

    fun getItem(id: Long) = list.find { item -> item.id == id }

    fun getSelectedCount(): Int = selection.size

    fun getSelectedCountInstance(clazz: KClass<out T>) = selection.count {
        it::class == clazz
    }

    override fun getItemCount(): Int = adapterList.size

    override fun getItemViewType(position: Int): Int = adapterList[position].type

    override fun add(item: T) = add(position = itemCount, item = item)

    override fun add(position: Int, item: T) {
        adapterList.add(position, item)
        notifyItemInserted(position)
    }

    fun add(type: Int, factory: ViewHolderFactory<T>) {
        factories[type] = factory
    }

    fun add(vararg types: Pair<Int, ViewHolderFactory<T>>) {
        types.map { (type, factory) -> factories[type] = factory }
    }

    fun removeFactory(type: Int) {
        factories.remove(type)
    }

    override fun remove(item: T) = remove(position(item))

    override fun remove(position: Int) {
        adapterList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun add(items: Iterable<T>) {
        val from = itemCount
        adapterList.addAll(items)
        val to = itemCount
        notifyItemRangeInserted(from, to)
    }

    override fun position(item: T): Int = adapterList.indexOf(item)

    override fun item(position: Int): T = adapterList[position]

    override fun changed(position: Int) {
        notifyItemChanged(position)
    }

    override fun changed(position: Int, payload: Any?) {
        notifyItemChanged(position, payload)
    }

    open fun replace(items: List<T>) = replace(
        items = items,
        differ = ItemDiffer(list, items)
    )

    override fun replace(items: List<T>, differ: Callback?) {
        if (adapterList == items) return

        if (differ == null) {
            adapterList.clear()
            adapterList.addAll(items)
            notifyDataSetChanged()
        } else {
            refresher.trySend(element = differ to items)
        }
    }

    override fun onViewRecycled(holder: ViewHolder<T>) {
        holder.unbind()
        holder.unbindListeners()
    }

    override fun register(listener: ItemViewListener<T>) {
        itemViewListeners.add(listener)
    }

    override fun registerLong(listener: ItemViewLongListener<T>) {
        itemViewLongListeners.add(listener)
    }

    override fun registerSelection(listener: SelectionListener<T>) {
        selectionListeners.add(listener)
    }

    override fun registerItemSelection(listener: ItemViewSelectionListener<T>) {
        itemViewSelectionListeners.add(listener)
    }

    protected fun notify(view: View, item: T, position: Int) {
        itemViewListeners.forEach { listener ->
            listener.onClick(view, item, position)
        }
    }

    protected fun notifyLong(view: View, item: T, position: Int): Boolean {
        itemViewLongListeners.forEach { listener ->
            if (listener.onLongClick(view, item, position)) return true
        }
        return false
    }

    protected fun notifySelection(item: T, isSelected: Boolean) {
        itemViewSelectionListeners.forEach { listener ->
            listener.onItemSelectionChanged(item, isSelected)
        }
    }

    protected fun notifySelection() {
        selectionListeners.forEach { listener ->
            listener.onSelectionChanged(selection.toList())
        }
    }

}

fun <T : Item<*>> ItemAdapter<T>.register(listener: ItemViewListener<Item<*>>) {
    register(listener as ItemViewListener<T>)
}

fun <T : Item<*>> ItemAdapter<T>.registerLong(listener: ItemViewLongListener<Item<*>>) {
    registerLong(listener as ItemViewLongListener<T>)
}

fun <T : Item<*>> ItemAdapter<T>.registerItemSelection(listener: ItemViewSelectionListener<Item<*>>) {
    registerItemSelection(listener as ItemViewSelectionListener<T>)
}


infix fun <T> Int.with(factory: ViewHolderFactory<*>): Pair<Int, ViewHolderFactory<T>> {
    return Pair(this, factory as ViewHolderFactory<T>)
}