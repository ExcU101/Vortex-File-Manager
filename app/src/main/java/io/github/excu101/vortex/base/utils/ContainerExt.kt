package io.github.excu101.vortex.base.utils

import io.github.excu101.vortex.base.Container
import io.github.excu101.vortex.base.ContainerHandler

suspend fun <S, E> ContainerHandler<S, E>.collectState(block: (S) -> Unit): Unit =
    container.collectState(block)

suspend fun <S, E> ContainerHandler<S, E>.collectEffect(block: (E) -> Unit): Unit =
    container.collectEffect(block)

suspend fun <S, E> Container<S, E>.collectState(block: (S) -> Unit): Unit = state.collect(block)

suspend fun <S, E> Container<S, E>.collectEffect(block: (E) -> Unit): Unit = effect.collect(block)
