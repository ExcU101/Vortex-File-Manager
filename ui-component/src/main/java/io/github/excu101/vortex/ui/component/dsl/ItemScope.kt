package io.github.excu101.vortex.ui.component.dsl

import io.github.excu101.vortex.ui.component.item.divider.DividerHeaderItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.item.text.attrs
import io.github.excu101.vortex.ui.component.list.adapter.Item

interface ItemScope<T : Item<*>> {

    fun add(item: T)

    fun remove(item: T)

    fun insert(index: Int, item: T)

    fun addOther(items: Collection<T>)

    fun removeOther(items: Collection<T>)

    fun toList(): List<T>

}

open class ItemScopeImpl<T : Item<*>> : ItemScope<T> {

    protected val list = mutableListOf<T>()

    override fun insert(index: Int, item: T) {
        list.add(index, item)
    }

    override fun add(item: T) {
        list.add(item)
    }

    override fun remove(item: T) {
        list.remove(item)
    }

    override fun addOther(items: Collection<T>) {
        list.addAll(items)
    }

    override fun removeOther(items: Collection<T>) {
        list.removeAll(items)
    }

    override fun toList(): List<T> {
        return list
    }
}

interface DividerItemScope : ItemScope<Item<*>>

interface GroupItemScope : ItemScope<Item<*>>

class DividerItemScopeImpl : ItemScopeImpl<Item<*>>(), DividerItemScope {
    override fun add(item: Item<*>) {
        super.add(DividerHeaderItem)
        super.add(item)
    }
}

class GroupItemScopeImpl(
    title: String,
    attrs: TextItem.Attrs = attrs { },
) : ItemScopeImpl<Item<*>>(), GroupItemScope {
    init {
        add(TextItem(title, attrs))
    }
}

inline fun ItemScope<Item<*>>.withDividers(scope: DividerItemScope.() -> Unit) {
    addOther(DividerItemScopeImpl().apply(scope).toList())
}

inline fun ItemScope<Item<*>>.withGroup(title: String, scope: GroupItemScope.() -> Unit) {
    addOther(GroupItemScopeImpl(title).apply(scope).toList())
}

inline fun <T : Item<*>> scope(block: ItemScope<T>.() -> Unit): List<T> {
    return ItemScopeImpl<T>().apply(block).toList()
}