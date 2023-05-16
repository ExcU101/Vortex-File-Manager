package io.github.excu101.vortex.navigation

import io.github.excu101.vortex.navigation.impl.NavPageControllerStackImpl
import io.github.excu101.vortex.navigation.page.PageController

typealias NavPageControllerStack = NavigationPageControllerStack

interface NavigationPageControllerStack : NavStack<PageController> {

    fun push(controller: PageController, isCurrent: Boolean = false)

    fun clear()

}

@Suppress("FunctionName")
fun NavPageControllerStack() = NavigationPageControllerStack()

@Suppress("FunctionName")
fun NavPageControllerStack(initial: PageController) = NavigationPageControllerStack(initial)

@Suppress("FunctionName")
fun NavigationPageControllerStack(): NavPageControllerStack = NavPageControllerStackImpl()

@Suppress("FunctionName")
fun NavigationPageControllerStack(initial: PageController): NavPageControllerStack =
    NavPageControllerStackImpl(initial)