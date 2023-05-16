package io.github.excu101.vortex.navigation.impl

import android.view.ViewGroup
import io.github.excu101.vortex.navigation.*
import io.github.excu101.vortex.navigation.page.PageController

internal typealias NavPageControllerImpl = NavigationPageControllerImpl

internal class NavigationPageControllerImpl internal constructor() : NavPageController {

    private var _wrapper: ViewGroup? = null

    override val stack: NavPageControllerStack = NavPageControllerStack()

    override fun navigate(
        controller: PageController,
        @Transition
        @Direction
        mode: Int,
    ): NavigationPageController {
        controller.attachNavigationController(controller = this)

        when {
            isForward(mode) -> {
                stack.push(controller = controller, isCurrent = true)
                navigateForward(
                    next = controller,
                    transition = mode and DIRECTION_FORWARD.inv()
                )
            }

            isBackward(mode) -> {
                stack.push(controller = controller, isCurrent = false)
            }

            else -> stack.push(controller = controller, isCurrent = false)
        }

        return super.navigate(controller, mode)
    }

    private fun navigateForward(
        current: PageController = stack.current!!,
        next: PageController,
        @Transition
        transition: Int,
    ) {
        current.onHide()

        _wrapper?.addView(next.getContentView())

        next.onFocus()
    }

    override fun bindWrapper(wrapper: ViewGroup?) {
        _wrapper = wrapper
    }

    override fun init(controller: PageController): NavigationPageController {
        if (!stack.isEmpty())
            for (i in 0 until stack.size) {
                val item = stack[i]

                if (item.isDestroyed) item.onDestroy()
            }

        stack.push(
            controller = controller,
            isCurrent = true
        )

        _wrapper?.addView(controller.getContentView())

        controller.attachNavigationController(controller = this)
        controller.onPrepare()

        return this
    }

    private fun isForward(mode: Int): Boolean = (mode and DIRECTION_FORWARD) == DIRECTION_FORWARD

    private fun isBackward(mode: Int): Boolean = (mode and DIRECTION_BACKWARD) == DIRECTION_BACKWARD

    private fun removeFlag(mode: Int, flag: Int): Int = mode and (flag.inv())

    override fun isBackActivated(): Boolean {
        if (stack.current?.onBackActivated() == true) return true
        if (stack.size > 1) {
            stack.removeLast()
            return true
        }

        return super.isBackActivated()
    }

}