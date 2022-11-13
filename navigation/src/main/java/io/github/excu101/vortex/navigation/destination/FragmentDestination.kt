package io.github.excu101.vortex.navigation.destination

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.excu101.vortex.navigation.NavigationGraph
import kotlin.reflect.KClass

abstract class FragmentDestination(
    override val route: String,
    override val graph: NavigationGraph<Fragment>?,
) : Destination<Fragment> {

    abstract fun createFragment(args: Bundle?): Fragment

}

@PublishedApi
internal class ReflectionFragmentDestination(
    private val fragmentClass: KClass<out Fragment>,
    override val graph: NavigationGraph<Fragment>?,
    override val route: String,
) : FragmentDestination(route, graph) {

    override fun createFragment(args: Bundle?): Fragment {
        return fragmentClass.constructors.first().call().apply {
            arguments = args
        }
    }

}