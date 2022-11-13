package io.github.excu101.vortex.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.excu101.vortex.navigation.destination.Destination
import io.github.excu101.vortex.navigation.destination.FragmentDestination
import io.github.excu101.vortex.navigation.navigator.FragmentNavigator
import io.github.excu101.vortex.navigation.navigator.Navigator

class FragmentNavigationController(
    private val graph: NavigationGraph<Fragment>,
) : NavigationController {

    companion object {
        fun from(
            graph: NavigationGraph<Fragment>,
            manager: FragmentManager,
            containerId: Int,
        ): FragmentNavigationController {
            return FragmentNavigationController(
                graph = graph
            ).bindNavigator(
                manager = manager,
                containerId = containerId
            )
        }
    }

    private val listeners = mutableListOf<DestinationChangedListener>()

    private var navigator: FragmentNavigator? = null

    override fun addDestination(dest: Destination<*>) {
        graph.addDestination(dest as Destination<Fragment>)
    }

    fun bindNavigator(
        manager: FragmentManager,
        containerId: Int,
    ): FragmentNavigationController {
        navigator = FragmentNavigator(
            manager = manager,
            containerId = containerId,
        )
        return this
    }

    fun bindOnBackPressedDispatcher(
        dispatcher: OnBackPressedDispatcher,
    ): FragmentNavigationController {
        onBackPressedCallback.remove()
        dispatcher.addCallback(onBackPressedCallback)

        return this
    }

    fun enableOnBackPressedCallback(enabled: Boolean): FragmentNavigationController {
        onBackPressedCallback.isEnabled = enabled
        return this
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            popBackStack()
        }
    }

    fun addListener(listener: DestinationChangedListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: DestinationChangedListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners(
        destination: Destination<*>,
        options: Array<Navigator.Option>,
    ) {
        listeners.forEach { listener ->
            listener.invoke(
                this,
                destination,
                options
            )
        }
    }

    override fun popBackStack(): Boolean {
        if (navigator == null) return false

        return popBackStack(navigator!!.stack.last(), true)
    }

    override fun popBackStack(
        route: String,
        inclusive: Boolean,
        requiresSavingState: Boolean,
    ): Boolean {
        if (navigator == null) return false
        if (navigator?.stack?.isEmpty() != false) return false

        navigator?.popBackStack(route)
        return true
    }

    override fun navigateUp() {
        if ((navigator?.stack?.size ?: 0) == 1) {
            enableOnBackPressedCallback(false)
        } else {
            popBackStack()
        }
    }

    override fun navigate(
        route: String,
        vararg options: Navigator.Option,
    ) {
        val dest = graph.routes.find { it.route == route } ?: return

        dest as FragmentDestination
        navigator?.navigate(destination = dest, options = options)
    }

}