package io.github.excu101.vortex.navigation.impl

import android.content.Context
import android.os.Bundle
import io.github.excu101.vortex.navigation.*
import io.github.excu101.vortex.navigation.NavigationPageControllerGraph.PageControllerProvider
import io.github.excu101.vortex.navigation.page.PageController

internal typealias NavPageControllerGraphImpl = NavigationPageControllerGraphImpl

private const val NAVIGATION_SAVED_COUNT = "nav_saved_count";
private const val NAVIGATION_ITEM_PREFIX = "nav_item_"

internal class NavigationPageControllerGraphImpl : NavPageControllerGraph {

    private var context: Context

    override val controller: NavPageController = NavPageController()

    internal constructor(context: Context, providers: Map<Int, PageControllerProvider>) {
        this.context = context
        _providers.putAll(providers)
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

    private fun generatePrefix(index: Int): String {
        return NAVIGATION_ITEM_PREFIX + index + "_"
    }

    override fun restoreNavigation(inState: Bundle) {
        val count = inState.getInt(NAVIGATION_SAVED_COUNT)
        var restored = 0

        if (count > 0) {
            for (i in 0 until count) {
                val prefix = generatePrefix(i)
                val controller = _providers[inState.getInt(prefix)]?.onProvide(
                    context = context,
                ) ?: continue

                if (controller.onRestoreInstance(inState, prefix)) {
                    if (restored > 0) stack.insertAt(0, controller)
                    else stack.push(controller, true)
                }

                restored++
            }
        }
    }

    override fun saveNavigation(outState: Bundle) {
        var saved = 0
        for (i in 0 until stack.size) {
            val prefix = generatePrefix(i)
            val controller = stack[i]

            if (controller.onSaveInstance(outState, prefix)) {
                outState.putInt(prefix, controller.getNavigationRoute())
                saved++
            }
        }
        if (saved > 0) outState.putInt(NAVIGATION_SAVED_COUNT, saved)
    }

}