package io.github.excu101.vortex.ui.screen.navigation

import android.content.Context
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import io.github.excu101.vortex.base.utils.logIt
import io.github.excu101.vortex.navigation.page.NavigationHostPageController
import io.github.excu101.vortex.navigation.page.PageController
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.ui.navigation.Navigation
import io.github.excu101.vortex.ui.navigation.Navigation.Storage

class HostNavigationPageController(
    val context: NavigationActivity,
) : NavigationHostPageController(context), MenuActionListener {

    override fun getControllerCount(): Int {
        return 2
    }

    override fun getNavigationRoute(): Int = Navigation.Host

    override fun onCreatePageController(position: Int): PageController {
        return Storage(context)
    }

    override fun onPrepareController(position: Int, controller: PageController) {
        controller.wrapper = this
    }

    override fun onHideController(position: Int, controller: PageController) {
        controller.wrapper = null
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onCreateView(context: Context, pager: ViewPager) {

    }

    override fun onMenuActionCall(action: MenuAction): Boolean {
        val controller = currentController ?: return false
        if (controller !is MenuActionListener) return false
        return controller.onMenuActionCall(action)
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onSaveInstance(bundle: Bundle, prefix: String): Boolean {
        for (i in 0 until 2) {
            adapter.getController(i)?.onSaveInstance(bundle, prefix + "host_")
        }

        return true
    }


    override fun onRestoreInstance(bundle: Bundle, prefix: String): Boolean {
        for (i in 0 until 2) {
            adapter.getController(i)?.logIt()?.onRestoreInstance(bundle, prefix + "host_")
        }

        return true
    }
}