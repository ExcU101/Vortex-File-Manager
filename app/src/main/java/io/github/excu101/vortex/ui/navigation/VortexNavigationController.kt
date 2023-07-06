package io.github.excu101.vortex.ui.navigation

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout.LayoutParams
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import io.github.excu101.vortex.navigation.NavigationPageController.NavigationRestorationControllerProvider
import io.github.excu101.vortex.navigation.impl.NavPageControllerBase
import io.github.excu101.vortex.navigation.page.NavigationPageLayout
import io.github.excu101.vortex.navigation.page.PageController
import io.github.excu101.vortex.ui.component.bar.Bar

class VortexNavigationController(
    private val context: Context,
    restoration: NavigationRestorationControllerProvider,
) : NavPageControllerBase(context, restoration) {

    fun onCreate() {
        root = NavigationPageLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                gravity = Gravity.CENTER
            }
        }

        bar = Bar(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                gravity = Gravity.BOTTOM
            }
        }
    }

    override fun onPrepareAttachNavigation(controller: PageController, mode: Int) {
        root?.addView(controller.getContentView())
        bar?.setTitleWithAnimation(
            title = controller.title,
            isReverse = isBackward(mode)
        )
    }

    override fun onPrepareDetachNavigation(controller: PageController, mode: Int) {
        root?.removeView(controller.getContentView())
    }

    var bar: Bar? = null
        private set

    var root: NavigationPageLayout? = null
        private set

    fun onDestroyWrapper() {
        root?.removeAllViews()
        bar = null
        root = null
    }

}