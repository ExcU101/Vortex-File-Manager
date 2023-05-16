package io.github.excu101.vortex.navigation.impl

import io.github.excu101.vortex.navigation.NavPageControllerStack
import io.github.excu101.vortex.navigation.NavStackListener
import io.github.excu101.vortex.navigation.page.PageController

internal typealias NavPageControllerStackImpl = NavigationPageControllerStackImpl

internal class NavigationPageControllerStackImpl : NavPageControllerStack {

    private val _stack = mutableListOf<PageController>()
    private val _listeners = mutableListOf<NavStackListener<PageController>>()
    private var _current = -1

    internal constructor()
    internal constructor(initial: PageController) {
        _stack.add(initial)
        _current = 0
    }

    override fun push(controller: PageController, isCurrent: Boolean) {
        _stack.add(controller)
        if (isCurrent) _current++

        notifyListeners()
    }

    override val size: Int
        get() = _stack.size

    override fun isEmpty(): Boolean = _stack.isEmpty()

    override var isLocked: Boolean = false

    override fun insertAt(index: Int, item: PageController) {
        if (index <= _current) {
            _stack.add(index, item)
            _current++
            notifyListeners()
        }
    }

    override fun removeAt(index: Int): PageController = _stack.removeAt(index = index)

    override fun attachListener(listener: NavStackListener<PageController>) {
        _listeners.add(listener)
    }

    override fun detachListener(listener: NavStackListener<PageController>) {
        _listeners.remove(listener)
    }

    private fun notifyListeners() {
        for (listener in _listeners)
            listener.onStackChanged(stack = this)
    }

    override fun clear() {
        _stack.clear()
        _current = -1
        notifyListeners()
    }

    override val currentIndex: Int
        get() = if (isLocked) 0 else _current

    override fun get(index: Int): PageController {
        return _stack[index]
    }

    override fun contains(item: PageController): Boolean = _stack.contains(item)
}