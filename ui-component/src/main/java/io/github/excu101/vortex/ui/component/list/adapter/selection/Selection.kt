package io.github.excu101.vortex.ui.component.list.adapter.selection

class Selection<K> : MutableIterable<K> {

    private val keys = mutableSetOf<K>()

    operator fun plusAssign(key: K) {
        add(key)
    }

    operator fun minusAssign(key: K) {
        remove(key)
    }

    operator fun contains(key: K): Boolean {
        return keys.contains(key)
    }

    fun clear() {
        keys.clear()
    }

    fun add(key: K): Boolean {
        return keys.add(key)
    }

    fun add(keys: Collection<K>): Boolean {
        return this.keys.addAll(keys)
    }

    fun remove(key: K): Boolean {
        return keys.remove(key)
    }

    val size: Int
        get() = keys.size

    override fun iterator(): MutableIterator<K> = keys.iterator()


}