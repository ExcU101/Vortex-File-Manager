package io.github.excu101.vortex.navigation.impl

import android.content.Context
import io.github.excu101.vortex.navigation.Direction
import io.github.excu101.vortex.navigation.NavPageController
import io.github.excu101.vortex.navigation.NavPageControllerGraph
import io.github.excu101.vortex.navigation.NavigationPageController
import io.github.excu101.vortex.navigation.NavigationPageControllerGraph
import io.github.excu101.vortex.navigation.NavigationPageControllerGraph.PageControllerProvider
import io.github.excu101.vortex.navigation.Transition
import io.github.excu101.vortex.navigation.page.PageController

internal typealias NavPageControllerGraphImpl = NavigationPageControllerGraphImpl

internal class NavigationPageControllerGraphImpl : NavPageControllerGraph {

    private var context: Context
    override val controller: NavPageController

    internal constructor(context: Context, providers: Map<Int, PageControllerProvider>) {
        this.context = context
        _providers.putAll(providers)
        controller = NavigationPageController(context) { restoreContext, route ->
            providers[route]?.onProvide(restoreContext)
        }
    }

    private val _providers = mutableMapOf<Int, PageControllerProvider>()

    override fun get(route: Int) = _providers[route] ?: throw Throwable()

    override fun addDestination(
        route: Int,
        provider: PageControllerProvider,
    ): NavigationPageControllerGraph {
        _providers[route] = provider
        return super.addDestination(route, provider)
    }

    override fun <A> navigate(
        route: Int,
        args: A?,
        @Transition
        @Direction
        mode: Int,
    ): NavigationPageControllerGraph {
        val con = getPageController(route)

        con.setArgs<A>(args)

        controller.navigate(
            controller = con,
            mode = mode
        )

        return super.navigate(route, args, mode)
    }

    private fun getPageController(route: Int): PageController {
        return get(route).onProvide(context)
    }

    override val count: Int
        get() = _providers.size

}