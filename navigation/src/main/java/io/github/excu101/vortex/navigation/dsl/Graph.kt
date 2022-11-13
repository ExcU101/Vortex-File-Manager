package io.github.excu101.vortex.navigation.dsl

import io.github.excu101.vortex.navigation.NavigationGraph

inline fun <T> navigation(block: NavigationGraph.Builder<T>.() -> Unit): NavigationGraph<T> {
    return NavigationGraph.Builder<T>().apply(block).build()
}