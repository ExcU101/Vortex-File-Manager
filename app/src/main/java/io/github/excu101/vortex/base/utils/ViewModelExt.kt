package io.github.excu101.vortex.base.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.excu101.vortex.base.Container
import io.github.excu101.vortex.base.ContainerHandler
import io.github.excu101.vortex.base.Logger
import io.github.excu101.vortex.base.impl.ContainerImpl
import io.github.excu101.vortex.base.impl.LoggerImpl

abstract class ViewModelContainerHandler<S, E>(initialState: S) : ViewModel(),
    ContainerHandler<S, E> {
    final override val container: Container<S, E> = container(initialState)
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
