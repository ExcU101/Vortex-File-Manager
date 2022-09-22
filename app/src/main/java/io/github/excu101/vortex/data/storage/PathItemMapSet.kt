package io.github.excu101.vortex.data.storage

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.data.PathItem

class MutablePathItemMapSet : PathItemMapSet() {

    private val mapper = hashMapOf<Path, PathItem>()
    private val extractor: (PathItem) -> Path =
        { it -> it.value }

    operator fun plusAssign(item: PathItem) {
        add(item)
    }

    operator fun minusAssign(item: PathItem) {
        remove(item)
    }

    fun add(element: PathItem): Boolean {
        return mapper.put(extractor(element), element) == null
    }

    fun addAll(elements: Iterable<PathItem>) {
        mapper.putAll(elements.associateBy(extractor))
    }

    fun addAll(elements: Array<out PathItem>) {
        elements.forEach(::add)
    }

    fun remove(element: PathItem): Boolean {
        return mapper.remove(extractor(element)) != null
    }

    fun removeAll(elements: Iterable<PathItem>) {
        elements.forEach(::remove)
    }

    fun clear() {
        mapper.clear()
    }

    override fun get(path: Path): PathItem {
        return mapper[path] ?: PathItem(path)
    }

    override fun iterator(): MutableIterator<PathItem> {
        return mapper.values.iterator()
    }

    override fun contains(element: PathItem): Boolean {
        return mapper.containsKey(extractor(element))
    }

    override val size: Int
        get() = mapper.values.size

}

abstract class PathItemMapSet :
    AbstractSet<PathItem>() {

    abstract operator fun get(path: Path): PathItem

    abstract override fun iterator(): Iterator<PathItem>

    abstract override val size: Int

}

fun storageItemMapSet(): PathItemMapSet {
    return MutablePathItemMapSet()
}

fun storageItemMapSet(vararg elements: PathItem): PathItemMapSet {
    return MutablePathItemMapSet().apply {
        addAll(elements = elements)
    }
}