package io.github.excu101.vortex.data

data class Section<I, V>(
    val identifier: I,
    val values: Collection<V>,
) {

    constructor(item: Pair<I, Collection<V>>) : this(item.first, item.second)

}