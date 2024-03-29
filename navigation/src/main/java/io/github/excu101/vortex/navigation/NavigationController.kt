package io.github.excu101.vortex.navigation

typealias NavController<T> = NavigationController<T>

interface NavigationController<T> {

    val stack: NavStack<T>

}

fun <T> NavController<T>.attachListener(listener: NavStackListener<T>): NavController<T> {
    stack.attachListener(listener)
    return this
}

fun <T> NavController<T>.detachListener(listener: NavStackListener<T>): NavController<T> {
    stack.detachListener(listener)
    return this
}