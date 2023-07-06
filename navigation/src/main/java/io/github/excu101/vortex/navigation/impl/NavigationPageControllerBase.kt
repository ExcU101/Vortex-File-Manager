package io.github.excu101.vortex.navigation.impl

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import io.github.excu101.vortex.navigation.DIRECTION_BACKWARD
import io.github.excu101.vortex.navigation.DIRECTION_FORWARD
import io.github.excu101.vortex.navigation.Direction
import io.github.excu101.vortex.navigation.NavPageController
import io.github.excu101.vortex.navigation.NavPageControllerStack
import io.github.excu101.vortex.navigation.NavigationPageController
import io.github.excu101.vortex.navigation.Transition
import io.github.excu101.vortex.navigation.current
import io.github.excu101.vortex.navigation.page.PageController
import io.github.excu101.vortex.navigation.previous
import io.github.excu101.vortex.navigation.removeLast

typealias NavPageControllerBase = NavigationPageControllerBase

private const val NAVIGATION_SAVED_COUNT = "nav_saved_count";
private const val NAVIGATION_ITEM_PREFIX = "nav_item_"

open class NavigationPageControllerBase(
    private val context: Context,
    private val restoration: NavigationPageController.NavigationRestorationControllerProvider,
) : NavPageController {

    override val stack: NavPageControllerStack = NavPageControllerStack()

    override fun navigate(
        controller: PageController,
        @Transition
        @Direction
        mode: Int,
    ): NavigationPageController {
        onPrepareAttachNavigation(controller, mode)
        controller.attachNavigationController(controller = this)
        controller.onPrepare()

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
                navigateBackward(
                    current = controller,
                    transition = mode and DIRECTION_BACKWARD.inv()
                )
            }

            else -> stack.push(controller = controller, isCurrent = false)
        }

        return super.navigate(controller, mode)
    }

    protected open fun onPrepareAttachNavigation(
        controller: PageController,
        @Transition
        @Direction
        mode: Int,
    ) {
    }

    protected open fun onPrepareDetachNavigation(
        controller: PageController,
        @Transition
        @Direction
        mode: Int,
    ) {
    }

    protected fun navigateForward(
        current: PageController = stack.current!!,
        next: PageController,
        @Transition
        transition: Int,
    ) {
        current.onHide()

        val currentView = current.getContentView()
        val nextView = next.getContentView()

        nextView.alpha = 0F
        val animator = ValueAnimator.ofFloat(0F, 1F)

        next.onFocus()
    }

    protected fun navigateBackward(
        current: PageController,
        previous: PageController = stack.previous!!,
        @Transition
        transition: Int,
    ) {
        current.onHide()

        previous.onFocus()
    }

    override fun init(controller: PageController): NavigationPageController {


        stack.push(
            controller = controller,
            isCurrent = true
        )

        onPrepareAttachNavigation(controller = controller, mode = -1)
        controller.attachNavigationController(controller = this)
        controller.onPrepare()

        return this
    }

    protected fun isForward(mode: Int): Boolean = (mode and DIRECTION_FORWARD) == DIRECTION_FORWARD

    protected fun isBackward(mode: Int): Boolean =
        (mode and DIRECTION_BACKWARD) == DIRECTION_BACKWARD

    protected fun removeFlag(mode: Int, flag: Int): Int = mode and (flag.inv())

    override fun isBackActivated(): Boolean {
        if (stack.current?.onBackActivated() == true) return true
        if (stack.size > 1) {
            stack.removeLast()
            return true
        }

        return super.isBackActivated()
    }

    private fun generatePrefix(index: Int): String {
        return NAVIGATION_ITEM_PREFIX + index + "_"
    }

    override fun restoreNavigation(inState: Bundle) {
        val count = inState.getInt(NAVIGATION_SAVED_COUNT)
        var restored = 0

        if (count > 0) {
            for (i in 0 until count) {
                val prefix = generatePrefix(i)
                val controller = restoration.onRestoreController(
                    context = context,
                    route = inState.getInt(prefix)
                ) ?: continue

                if (controller.onRestoreInstance(inState, prefix)) {
                    if (restored > 0) stack.insertAt(0, controller)
                    else init(controller)
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

    override fun clear() {
        if (!stack.isEmpty()) {
            for (i in 0 until stack.size) {
                val item = stack[i]

                if (!item.isDestroyed) {
                    item.onDestroy()
                    item.detachNavigationController()
                }
            }
        }
        stack.clear()
    }

}