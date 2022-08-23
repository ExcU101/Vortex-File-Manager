package io.github.excu101.vortex.data

import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.coroutines.flow.MutableStateFlow

class MutableStorageItemMapSet : StorageItemMapSet() {

    private val mapper = hashMapOf<Path, FileUnit>()
    private val extractor = FileUnit::path

    fun add(element: FileUnit): Boolean {
        return mapper.put(extractor(element), element) == null
    }

    fun remove(element: FileUnit): Boolean {
        return mapper.put(extractor(element), element) != null
    }

    override fun get(path: Path): FileUnit {
        return mapper[path] ?: FileUnit(path)
    }

    override fun iterator(): MutableIterator<FileUnit> {
        return mapper.values.iterator()
    }

    override fun contains(element: FileUnit): Boolean {
        return mapper.contains(extractor(element))
    }

    override val size: Int
        get() = mapper.values.size

}

abstract class StorageItemMapSet : AbstractSet<FileUnit>() {

    abstract operator fun get(path: Path): FileUnit

    abstract override fun iterator(): MutableIterator<FileUnit>

    abstract override val size: Int

}