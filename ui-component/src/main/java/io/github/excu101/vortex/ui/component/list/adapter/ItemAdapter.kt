package io.github.excu101.vortex.ui.component.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.list.adapter.diff.ItemDiffer
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.list.adapter.listener.ClickListenerRegister
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor

interface ViewHolderFactory<T> {
    fun produceView(parent: ViewGroup): View

    fun produceViewHolder(child: View): ViewHolder<T>
}

class UnsupportedViewTypeException(
    viewType: Int,
) : Throwable("Unsupported view type (viewType: $viewType)")

open class ItemAdapter<T : Item<*>> : RecyclerView.Adapter<ViewHolder<T>>, EditableAdapter<T>,
    ClickListenerRegister<T> {

    init {
        setHasStableIds(true)
    }

    // key: viewType, value: viewHolder factory
    private val factories = mutableMapOf<Int, ViewHolderFactory<T>>()

    val viewTypes: Int
        get() = factories.keys.size

    private val itemViewListeners = mutableListOf<ItemViewListener<T>>()
    private val itemViewLongListeners = mutableListOf<ItemViewLongListener<T>>()

    protected val adapterList = mutableListOf<T>()
    val list: List<T>
        get() = adapterList

    constructor(vararg types: Pair<Int, ViewHolderFactory<T>>) {
        factories.putAll(types)
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private val refresher =
        CoroutineScope(Dispatchers.Default).actor<Pair<DiffUtil.Callback, List<T>>>(capacity = Channel.CONFLATED) {
            for ((differ, items) in channel) {
                val result = DiffUtil.calculateDiff(differ)
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
            notify(view, item, holder.bindingAdapterPosition)
        }
        holder.bindLongListener { view ->
            notifyLong(view, item, holder.bindingAdapterPosition)
        }
    }

    open fun isSelected(position: Int): Boolean {
        return false
    }

    open fun isSelected(item: T): Boolean {
        return false
    }

    inline fun findElement(predicate: (T) -> Boolean): T? {
        return list.find(predicate)
    }

    inline fun findPosition(predicate: (Int) -> Boolean): Int {
        var position = -1

        for (i in list.indices) {
            if (predicate(i)) {
                position = i
            }
        }

        return position
    }

    inline fun findPositionByElement(predicate: (T) -> Boolean): Int {
        var position = -1

        for (i in list.indices) {
            if (predicate(item(i))) {
                position = i
            }
        }

        return position
    }

    override fun onBindViewHolder(
        holder: ViewHolder<T>,
        position: Int,
        payloads: List<Any>,
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemId(position: Int): Long = item(position).id

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

    open fun replace(items: List<T>) = replace(
        items = items,
        differ = ItemDiffer(list, items)
    )

    override fun replace(items: List<T>, differ: DiffUtil.Callback?) {
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
        super.onViewRecycled(holder)
        holder.unbind()
        holder.unbindListeners()
    }

    override fun register(listener: ItemViewListener<T>) {
        itemViewListeners.add(listener)
    }

    override fun registerLong(listener: ItemViewLongListener<T>) {
        itemViewLongListeners.add(listener)
    }

    protected fun notifyAdd() {

    }

    protected fun notify(view: View, item: T, position: Int) {
        itemViewListeners.forEach { listener ->
            listener.onClick(view, item, position)
        }
    }

    protected fun notifyLong(view: View, item: T, position: Int): Boolean {
        var clicked = false

        itemViewLongListeners.forEach { listener ->
            clicked = listener.onClick(view, item, position)
        }

        return clicked
    }

}