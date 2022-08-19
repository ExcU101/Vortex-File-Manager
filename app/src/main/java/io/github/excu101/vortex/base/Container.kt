package io.github.excu101.vortex.base

import io.github.excu101.vortex.base.utils.ContainerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Container<S, E> {

    val effect: Flow<E>

    val state: StateFlow<S>

    val collector: Collector<S>

    val logger: Logger

    fun emit(block: suspend ContainerScope<S, E>.() -> Unit)

}