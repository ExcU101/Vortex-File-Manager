package io.github.excu101.vortex.navigation

typealias NavStack<T> = NavigationStack<T>
typealias NavStackListener<T> = NavigationStack.NavigationStackListener<T>

interface NavigationStack<T> {
    val size: Int
    val currentIndex: Int
    var isLocked: Boolean

    fun isEmpty(): Boolean

    operator fun get(index: Int): T

    fun contains(item: T): Boolean

    fun insertAt(index: Int, item: T)

    fun removeAt(index: Int): T

    fun attachListener(listener: NavigationStackListener<T>)

    fun detachListener(listener: NavigationStackListener<T>)

    fun interface NavigationStackListener<T> {
        fun onStackChanged(stack: NavStack<T>)
    }
}

val <T> NavStack<T>.previous
    get() = if (isEmpty()) null else get(currentIndex - 1)

val <T> NavStack<T>.next
    get() = if (isEmpty()) null else get(currentIndex + 1)

val <T> NavStack<T>.first
    get() = if (isEmpty()) null else get(0)

val <T> NavStack<T>.last
    get() = if (isEmpty()) null else get(size - 1)

val <T> NavStack<T>.current
    get() = if (isEmpty()) null else get(currentIndex)

fun <T> NavStack<T>.removeLast() {
    removeAt(size - 1)
}