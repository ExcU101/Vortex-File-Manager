package io.github.excu101.vortex.ui.navigation

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import io.github.excu101.vortex.navigation.NavigationController
import io.github.excu101.vortex.navigation.NavigationGraph
import io.github.excu101.vortex.navigation.destination.Destination
import io.github.excu101.vortex.navigation.destination.FragmentDestination
import io.github.excu101.vortex.navigation.navigator.Navigator
import io.github.excu101.vortex.ui.component.FragmentAdapter

class ViewPagerNavigationController(
    private val graph: NavigationGraph<Fragment>,
    private val adapter: FragmentAdapter,
    private val pager: ViewPager2
) : NavigationController {

    private val navigator = ViewPagerNavigator(
        adapter = adapter,
        pager = pager
    )

    fun wrapCurrentSelection(fragment: Fragment) = navigator.wrapCurrentSelection(fragment)

    override fun addDestination(dest: Destination<*>) {
        graph.addDestination(dest as Destination<Fragment>)
    }

    override fun popBackStack(): Boolean {
        return popBackStack(navigator.stack.last(), true)
    }

    override fun popBackStack(
        route: String,
        inclusive: Boolean,
        requiresSavingState: Boolean
    ): Boolean {
        if (navigator.stack.isEmpty()) return false

        navigator.popBackStack(route)
        return true
    }

    override fun navigateUp() {
        popBackStack()
    }

    override fun navigate(route: String, vararg options: Navigator.Option) {
        val dest = graph.routes.find { it.route == route } ?: return

        dest as FragmentDestination
        navigator.navigate(destination = dest, options = options)
    }


}