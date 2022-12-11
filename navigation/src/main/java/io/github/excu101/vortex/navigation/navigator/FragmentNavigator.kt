package io.github.excu101.vortex.navigation.navigator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.excu101.vortex.navigation.destination.Destination
import io.github.excu101.vortex.navigation.destination.FragmentDestination

class FragmentNavigator(
    private val manager: FragmentManager,
    private val containerId: Int,
) : Navigator<Destination<Fragment>> {

    private var _stack = mutableListOf<String>()
    val stack: List<String>
        get() = _stack

    override fun popBackStack(route: String, requiresSavingState: Boolean) {
        if (manager.isStateSaved) return
        if (_stack.isEmpty()) return

        if (requiresSavingState) {

        } else {
            manager.popBackStack(
                route,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        /**
         *  Removing backStack from end to start to current route
         **/
        for (i in _stack.lastIndex..0) {
            if (_stack[i] != route) {
                _stack.remove(route)
            } else {
                break
            }
        }
    }

    override fun navigate(
        destination: Destination<Fragment>,
        vararg options: Navigator.Option,
    ) {
        if (manager.isStateSaved) return
        if (destination !is FragmentDestination) return

        val isInitial = _stack.isEmpty()

        val route = destination.route
        var arguments: Bundle? = null
        val elements = mutableMapOf<View, String>()

        for (option in options) {
            with(option) {
                if (this is Arguments) {
                    arguments = original
                }
                if (this is SharedElement) {
                    elements[view] = name
                }
            }
        }

        val fragment = destination.createFragment(args = arguments)
        val transaction = manager.beginTransaction()

        transaction.replace(containerId, fragment)
        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)

        if (!isInitial) {
            transaction.addToBackStack(route)
        }

        elements.forEach { (view, name) ->
            transaction.addSharedElement(view, name)
        }

        transaction.commit()

        _stack += route
    }

    class Arguments(val original: Bundle) : Navigator.Option

    class SharedElement(val view: View, val name: String) : Navigator.Option

}