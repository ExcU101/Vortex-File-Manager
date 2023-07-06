package io.github.excu101.vortex.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import io.github.excu101.vortex.navigation.NavigationPageController.NavigationRestorationControllerProvider
import io.github.excu101.vortex.navigation.impl.NavPageControllerBase
import io.github.excu101.vortex.navigation.page.PageController

typealias NavPageController = NavigationPageController
typealias NavRestorer = NavigationRestorationControllerProvider

interface NavigationPageController : NavController<PageController> {

    override val stack: NavigationPageControllerStack

    fun interface NavigationRestorationControllerProvider {
        fun onRestoreController(context: Context, route: Int): PageController?
    }

    fun navigate(
        controller: PageController,
        @Transition
        @Direction
        mode: Int = mode(),
    ): NavigationPageController = this

    fun restoreNavigation(inState: Bundle)

    fun saveNavigation(outState: Bundle)

    fun isBackActivated(): Boolean = false

    fun init(controller: PageController): NavigationPageController = this

    fun clear()
}

@Suppress("FunctionName")
fun NavPageController(
    context: Context,
    restoration: NavigationRestorationControllerProvider,
) = NavigationPageController(context, restoration)

@Suppress("FunctionName")
fun NavigationPageController(
    context: Context,
    restoration: NavigationRestorationControllerProvider,
): NavPageController = NavPageControllerBase(context, restoration)

val NavPageController.current: PageController?
    get() = stack.current

val NavPageController.currentView: View?
    get() = current?.getContentView()

fun NavPageController.onFocus(): NavPageController {
    current?.onFocus()
    return this
}

fun NavPageController.onHide(): NavPageController {
    current?.onHide()
    return this
}

fun NavPageController.onPrepare(): NavPageController {
    current?.onPrepare()
    return this
}

fun NavPageController.onDestroy(): NavPageController {
    current?.onDestroy()
    return this
}

fun NavPageController.onBackActivated(): Boolean {
    if (current?.onBackActivated() == true) return true

    if (!stack.isEmpty()) {
        navigateBack()
        return true
    }

    return false
}

fun NavPageController.onActivityResult(
    request: Int,
    result: Int,
    data: Intent?,
): Boolean = current?.onActivityResult(request, result, data) == true

fun NavPageController.navigateBack(
    controller: PageController? = stack.previous,
    @Transition
    transition: Int = TRANSITION_FADE,
    duration: Int = 300,
): NavPageController {
    if (controller == null) return this

    return navigate(
        controller = controller,
        mode = mode(
            transition = transition,
            direction = DIRECTION_BACKWARD,
            duration = duration
        ),
    )
}

fun NavPageController.navigateForward(
    controller: PageController? = stack.next,
    @Transition
    transition: Int = TRANSITION_FADE,
    duration: Int = 300,
): NavPageController {
    if (controller == null) return this

    return navigate(
        controller = controller,
        mode = mode(
            transition = transition,
            direction = DIRECTION_FORWARD,
            duration = duration
        ),
    )
}

