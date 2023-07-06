package io.github.excu101.vortex.base.utils

import io.github.excu101.vortex.base.Container
import io.github.excu101.vortex.base.ContainerHandler
import kotlinx.coroutines.CoroutineScope

data class IntentScope<S, E>(val scope: ContainerScope<S, E>) {
    val state: S
        get() = scope.getState()

}

data class StateContext<S>(val state: S)

class ContainerScope<S, E>(
    val getState: () -> S,
    val reduce: suspend ((S) -> S) -> Unit,
    val effect: suspend (E) -> Unit,
    val message: suspend (String) -> Unit,
)

inline fun <S, E> ContainerHandler<S, E>.intent(crossinline block: suspend IntentScope<S, E>.() -> Unit) {
    container.emit {
        IntentScope(scope = this).block()
    }
}

suspend inline fun <S, E> IntentScope<S, E>.reduce(crossinline block: StateContext<S>.() -> S) {
    scope.apply {
        reduce { reducer ->
            StateContext(reducer).block()
        }
    }
}

suspend inline fun <S, E> IntentScope<S, E>.state(crossinline block: S.() -> S) {
    reduce { state.let(block) }
}

suspend inline fun <S, E> IntentScope<S, E>.side(effect: E) {
    scope.effect(effect)
}

suspend fun <S, E> IntentScope<S, E>.logger(message: String) {
    scope.message(message)
}

inline fun <S, E> ContainerHandler<S, E>.select(index: Int, block: S.() -> Unit) {
    container.select(index = index, block = block)
}

inline fun <S, E> Container<S, E>.select(index: Int, block: S.() -> Unit) {
    block(collector.states[index])
}