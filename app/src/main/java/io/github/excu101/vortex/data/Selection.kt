package io.github.excu101.vortex.data

class Selection<K> : Iterable<K> {

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

    fun add(key: K): Boolean {
        return keys.add(key)
    }

    fun remove(key: K): Boolean {
        return keys.remove(key)
    }

    val size: Int
        get() = keys.size

    override fun iterator() = keys.iterator()

}