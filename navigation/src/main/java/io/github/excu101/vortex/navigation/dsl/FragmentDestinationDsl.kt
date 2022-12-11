package io.github.excu101.vortex.navigation.dsl

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.excu101.vortex.navigation.NavigationGraph
import io.github.excu101.vortex.navigation.destination.FragmentDestination
import io.github.excu101.vortex.navigation.destination.ReflectionFragmentDestination

inline fun <reified T : Fragment> NavigationGraph.Builder<Fragment>.fragment(
    route: String,
) {
    addDestination(ReflectionFragmentDestination(T::class, route = route, graph = null))
}

fun NavigationGraph.Builder<Fragment>.fragment(
    route: String,
    factory: FragmentFactory<out Fragment>,
) {
    addDestination(object : FragmentDestination(route = route, graph = null) {
        override fun createFragment(args: Bundle?): Fragment = factory.create(args)
    })
}

inline fun <reified T : Fragment> NavigationGraph.Builder<Fragment>.fragment(
    route: String,
    crossinline onCreateFragment: (Bundle?) -> T,
) {
    addDestination(object : FragmentDestination(route = route, graph = null) {
        override fun createFragment(args: Bundle?): T {
            return onCreateFragment.invoke(args)
        }
    })
}