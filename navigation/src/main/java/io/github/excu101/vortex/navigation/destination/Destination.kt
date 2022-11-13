package io.github.excu101.vortex.navigation.destination

import io.github.excu101.vortex.navigation.NavigationGraph

interface Destination<T> {

    val graph: NavigationGraph<T>?

    val route: String

}