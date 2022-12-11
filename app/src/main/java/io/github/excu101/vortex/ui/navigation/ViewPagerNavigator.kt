package io.github.excu101.vortex.ui.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import io.github.excu101.vortex.navigation.destination.Destination
import io.github.excu101.vortex.navigation.destination.FragmentDestination
import io.github.excu101.vortex.navigation.navigator.FragmentNavigator
import io.github.excu101.vortex.navigation.navigator.Navigator
import io.github.excu101.vortex.ui.component.FragmentAdapter

class ViewPagerNavigator(
    private val adapter: FragmentAdapter,
    private val pager: ViewPager2
) : Navigator<Destination<Fragment>> {

    private var _stack = mutableMapOf<String, Fragment>()
    val stack: Set<String>
        get() = _stack.keys

    override fun navigate(destination: Destination<Fragment>, vararg options: Navigator.Option) {
        if (destination !is FragmentDestination) return

        val route = destination.route
        var arguments: Bundle? = null
        val elements = mutableMapOf<View, String>()

        for (option in options) {
            with(option) {
                if (this is FragmentNavigator.Arguments) {
                    arguments = original
                }
                if (this is FragmentNavigator.SharedElement) {
                    elements[view] = name
                }
            }
        }

        val fragment = destination.createFragment(arguments)

        adapter.addFragment(fragment)
        _stack[route] = fragment
        pager.currentItem = adapter.itemCount
    }

    override fun popBackStack(route: String, requiresSavingState: Boolean) {
        if (_stack.isEmpty()) return

        if (requiresSavingState) {

        } else {
            _stack[route]?.let { adapter.pop(it) }
        }
    }

}