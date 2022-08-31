package io.github.excu101.vortex.data.storage

import io.github.excu101.filesystem.fs.path.Path

class MutableStorageItemMapSet : StorageItemMapSet() {

    private val mapper = hashMapOf<Path, StorageItem>()
    private val extractor: (StorageItem) -> Path = { it -> it.value }

    fun add(element: StorageItem): Boolean {
        return mapper.put(extractor(element), element) == null
    }

    fun addAll(elements: List<StorageItem>) {
        elements.forEach(::add)
    }

    fun remove(element: StorageItem): Boolean {
        return mapper.put(extractor(element), element) != null
    }

    override fun get(path: Path): StorageItem {
        return mapper[path] ?: StorageItem(path)
    }

    override fun iterator(): MutableIterator<StorageItem> {
        return mapper.values.iterator()
    }

    override fun contains(element: StorageItem): Boolean {
        return mapper.contains(extractor(element))
    }

    override val size: Int
        get() = mapper.values.size

}

abstract class StorageItemMapSet : AbstractSet<StorageItem>() {

    abstract operator fun get(path: Path): StorageItem

    abstract override fun iterator(): MutableIterator<StorageItem>

    abstract override val size: Int

    fun toMutable() = MutableStorageItemMapSet()

}

fun storageItemMapSet(): StorageItemMapSet {
    return MutableStorageItemMapSet()
}