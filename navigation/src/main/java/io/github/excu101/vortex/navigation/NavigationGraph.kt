package io.github.excu101.vortex.navigation

import io.github.excu101.vortex.navigation.destination.Destination

interface NavigationGraph<T> {

    val routes: List<Destination<T>>

    fun addDestination(destination: Destination<T>)

    class Builder<T> {
        private val destinations = mutableListOf<Destination<T>>()

        fun addDestination(destination: Destination<T>) {
            destinations += destination
        }

        fun build(): NavigationGraph<T> {
            return NavigationGraphImpl(destinations)
        }
    }
}

class NavigationGraphImpl<T>(
    private val _routes: MutableList<Destination<T>>,
) : NavigationGraph<T> {

    override val routes: List<Destination<T>>
        get() = _routes

    override fun addDestination(destination: Destination<T>) {
        _routes.add(destination)
    }
}