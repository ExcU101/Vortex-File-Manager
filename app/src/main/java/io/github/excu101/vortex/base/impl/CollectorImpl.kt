package io.github.excu101.vortex.base.impl

import io.github.excu101.vortex.base.Collector

class CollectorImpl<S> : Collector<S> {

    private val _states = mutableListOf<S>()

    override val states: List<S>
        get() = _states

    override fun emit(state: S) {
        _states.add(state)
    }

}