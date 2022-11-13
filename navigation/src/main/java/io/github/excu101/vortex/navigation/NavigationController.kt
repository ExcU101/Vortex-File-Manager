package io.github.excu101.vortex.navigation

import io.github.excu101.vortex.navigation.navigator.Navigator.Option
import io.github.excu101.vortex.navigation.destination.Destination

typealias DestinationChangedListener = (
    controller: NavigationController,
    dest: Destination<*>,
    options: Array<Option>,
) -> Unit

interface NavigationController {

    fun addDestination(dest: Destination<*>)

    fun popBackStack(): Boolean

    fun popBackStack(route: String, inclusive: Boolean): Boolean = popBackStack(
        route = route,
        inclusive = inclusive,
        requiresSavingState = false
    )

    fun popBackStack(
        route: String,
        inclusive: Boolean,
        requiresSavingState: Boolean,
    ): Boolean

    fun navigateUp()

    fun navigate(
        route: String,
        vararg options: Option,
    )

}