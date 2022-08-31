package io.github.excu101.vortex.data

class Sections<I, V> : Iterable<Section<I, V>> {

    constructor()

    constructor(initial: Section<I, V>) {
        _sections.add(initial)
    }

    constructor(initial: List<Section<I, V>>) {
        _sections.addAll(initial)
    }

    constructor(vararg sections: Section<I, V>) {
        _sections.addAll(sections)
    }

    private val _sections = mutableListOf<Section<I, V>>()

    val sectionCount
        get() = _sections.size

    val identifiers: List<I>
        get() {
            val output = mutableListOf<I>()
            _sections.forEach {
                output.add(it.identifier)
            }

            return output
        }

    val values: List<V>
        get() {
            val output = mutableListOf<V>()

            _sections.forEach {
                output.addAll(it.values)
            }

            return output
        }

    val itemsCount: Int
        get() = _sections.fold(initial = 0) { prev, current ->
            current.values.size + prev
        }

    override fun iterator(): Iterator<Section<I, V>> = _sections.iterator()

    operator fun get(index: Int) = _sections[index]

    operator fun contains(section: Section<I, V>) = section in _sections

    fun addSection(section: Section<I, V>) {
        _sections.add(section)
    }

    fun addSections(vararg sections: Section<I, V>) {
        _sections.addAll(sections)
    }

    fun removeSection(section: Section<I, V>) {
        _sections.remove(section)
    }

}