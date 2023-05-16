package io.github.excu101.vortex.base.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.excu101.vortex.base.Container
import io.github.excu101.vortex.base.ContainerHandler
import io.github.excu101.vortex.base.Logger
import io.github.excu101.vortex.base.impl.ContainerImpl
import io.github.excu101.vortex.base.impl.LoggerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

abstract class ViewModelContainerHandler<S, E>(initialState: S) : ViewModel(),
    ContainerHandler<S, E> {
    final override val container: Container<S, E> = container(initialState)
}

fun <S, E> container(
    initialState: S,
    tag: String = "Loggable",
) = container<S, E>(initialState = initialState, logger = LoggerImpl(tag))

fun <S, E> container(
    initialState: S,
    logger: Logger,
) = ContainerImpl<S, E>(
    initState = initialState,
    logger = logger,
    parentScope = CancelableScope(SupervisorJob() + Main.immediate)
)

class CancelableScope(override val coroutineContext: CoroutineContext) : Closeable, CoroutineScope {
    override fun close() {
        coroutineContext.cancel()
    }
}

internal fun <S, E> ViewModel.container(
    initialState: S,
    tag: String = "Loggable",
): Container<S, E> = container(initialState = initialState, logger = LoggerImpl(tag = tag))

internal fun <S, E> ViewModel.container(
    initialState: S,
    logger: Logger,
): Container<S, E> = ContainerImpl(
    initState = initialState,
    parentScope = viewModelScope,
    logger = logger
)
