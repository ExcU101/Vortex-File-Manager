package io.github.excu101.vortex.navigation

import android.view.View
import android.view.ViewGroup
import io.github.excu101.vortex.navigation.impl.NavPageControllerImpl
import io.github.excu101.vortex.navigation.page.PageController

typealias NavPageController = NavigationPageController

interface NavigationPageController : NavController<PageController> {
    override val stack: NavigationPageControllerStack

    fun bindWrapper(wrapper: ViewGroup?)

    fun navigate(
        controller: PageController,
        @Transition
        @Direction
        mode: Int = mode(),
    ): NavigationPageController = this

    fun isBackActivated(): Boolean = false

    fun init(controller: PageController): NavigationPageController = this
}

@Suppress("FunctionName")
fun NavPageController() = NavigationPageController()

@Suppress("FunctionName")
fun NavigationPageController(): NavPageController = NavPageControllerImpl()

val NavPageController.current: PageController?
    get() = stack.current

val NavPageController.currentView: View?
    get() = current?.getContentView()

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

