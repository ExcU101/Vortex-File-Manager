package io.github.excu101.vortex.data

import io.github.excu101.filesystem.fs.path.Path

class PathItemMapSet : AbstractMutableSet<PathItem>() {

    private val convertor = PathItem::value
    private val map = mutableMapOf<Path, PathItem>()

    operator fun get(path: Path) = map[path]

    override fun add(element: PathItem): Boolean {
        map[convertor(element)] = element
        return contains(element)
    }

    override fun contains(element: PathItem): Boolean = map[convertor(element)] != null

    override val size: Int
        get() = map.values.size

    override fun iterator(): MutableIterator<PathItem> = map.values.iterator()

}