package io.github.excu101.vortex.ui.component.dsl

import io.github.excu101.vortex.ui.component.list.adapter.Item

interface ItemScope<T : Item<*>> {

    fun add(item: T)

    fun remove(item: T)

    fun addOther(items: Collection<T>)

    fun removeOther(items: Collection<T>)

    fun toList(): List<T>

}

@PublishedApi
internal class ItemScopeImpl<T : Item<*>> : ItemScope<T> {

    private val list = mutableListOf<T>()

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

inline fun <T : Item<*>> scope(block: ItemScope<T>.() -> Unit): List<T> {
    return ItemScopeImpl<T>().apply(block).toList()
}