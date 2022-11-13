package io.github.excu101.vortex.navigation.navigator

import io.github.excu101.vortex.navigation.destination.Destination

interface Navigator<T : Destination<*>> {

    fun popBackStack(route: String, requiresSavingState: Boolean = false)

    fun navigate(
        destination: T,
        vararg options: Option,
    )

    interface Option

}