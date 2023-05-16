package io.github.excu101.vortex.navigation

import android.content.Context
import android.os.Bundle
import io.github.excu101.vortex.navigation.NavigationPageControllerGraph.PageControllerProvider
import io.github.excu101.vortex.navigation.impl.NavPageControllerGraphImpl
import io.github.excu101.vortex.navigation.page.PageController

typealias NavPageControllerGraph = NavigationPageControllerGraph

interface NavigationPageControllerGraph : NavGraph {

    val controller: NavPageController

    fun interface PageControllerProvider {
        fun onProvide(context: Context): PageController
    }

    operator fun get(route: Int): PageControllerProvider

    fun addDestination(
        route: Int,
        provider: PageControllerProvider,
    ): NavigationPageControllerGraph = this

    fun <A> navigate(
        route: Int,
        args: A? = null,
        @Transition
        @Direction
        mode: Int = mode(),
    ): NavigationPageControllerGraph = this

    fun restoreNavigation(inState: Bundle)

    fun saveNavigation(outState: Bundle)

}

fun NavPageControllerGraph(
    context: Context,
    providers: Map<Int, PageControllerProvider> = mapOf(),
): NavPageControllerGraph = NavPageControllerGraphImpl(context, providers)

val NavPageControllerGraph.stack: NavigationPageControllerStack
    get() = controller.stack