package io.github.excu101.vortex.ui.screen.navigation

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout.LayoutParams
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import io.github.excu101.vortex.navigation.NavPageControllerGraph
import io.github.excu101.vortex.navigation.current
import io.github.excu101.vortex.navigation.page.NavigationHostPageController
import io.github.excu101.vortex.navigation.page.TitledPageController
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.ui.navigation.graph
import io.github.excu101.vortex.ui.screen.storage.Actions
import io.github.excu101.vortex.ui.screen.storage.StoragePageController
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageFragment

class NavigationPageController(
    context: Context,
    graph: NavPageControllerGraph = context.graph,
) : NavigationHostPageController(context, graph), MenuActionListener {

    private var bar: Bar? = null

    override fun onMenuActionCall(action: MenuAction): Boolean {
        val current = graph.controller.current
        if (current is MenuActionListener) {
            if (current.onMenuActionCall(action)) return true
        }

        return when (action.id) {
            else -> false
        }
    }

    override fun onPageSelected(position: Int) {
        val controller = adapter.getController(position)
        controller?.onFocus()
        if (controller is TitledPageController)
            bar?.setTitleWithAnimation(controller.title)

        if (controller is StoragePageController) {
            bar?.replaceItems(Actions.BarActions)
        }
    }

    override fun onCreateView(context: Context): View {
        val root = super.onCreateView(context) as ViewGroup
        bar = Bar(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER
            }
            registerListener(this@NavigationPageController)
        }

        root.addView(bar, 1)

        return root
    }

}