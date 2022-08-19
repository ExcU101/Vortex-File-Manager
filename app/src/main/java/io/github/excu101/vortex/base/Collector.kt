package io.github.excu101.vortex.base

interface Collector<S> {

    val states: List<S>

    fun emit(state: S)

}