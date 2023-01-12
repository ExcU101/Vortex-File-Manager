package io.github.excu101.vortex.base.impl

import io.github.excu101.vortex.base.Collector
import io.github.excu101.vortex.base.Container
import io.github.excu101.vortex.base.Logger
import io.github.excu101.vortex.base.utils.ContainerScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ContainerImpl<S, E>(
    initState: S,
    override val logger: Logger,
    private val parentScope: CoroutineScope,
) : Container<S, E> {

    private val _effect = Channel<E>()
    private val _state = MutableStateFlow(initState)
    private val _collector = CollectorImpl<S>()

    override val collector: Collector<S>
        get() = _collector

    override val effect: Flow<E>
        get() = _effect.consumeAsFlow()

    override val state: StateFlow<S>
        get() = _state.asStateFlow()

    private val scope: ContainerScope<S, E> = ContainerScope(
        getState = { state.value },
        reduce = { reducer ->
            emitState(reducer(state.value))
        },
        effect = { effect ->
            emitEffect(effect)
        },
        message = { message ->
            logger.log(message)
        }
    )

    private fun emitState(state: S) {
        parentScope.launch {
            _state.emit(state)
            _collector.emit(state)
        }
    }

    private fun emitEffect(effect: E) {
        parentScope.launch(Unconfined) {
            _effect.send(effect)
        }
    }

    override fun emit(block: suspend ContainerScope<S, E>.() -> Unit) {
        parentScope.launch(Unconfined) {
            scope.block()
        }
    }

}