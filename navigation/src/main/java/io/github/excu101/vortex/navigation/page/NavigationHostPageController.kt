package io.github.excu101.vortex.navigation.page

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import androidx.annotation.CallSuper
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import io.github.excu101.vortex.navigation.NavPageControllerGraph
import io.github.excu101.vortex.navigation.NavStack
import io.github.excu101.vortex.navigation.NavStackListener
import io.github.excu101.vortex.navigation.NavigationPageAdapter
import io.github.excu101.vortex.navigation.NavigationPageAdapter.ControllerProvider
import io.github.excu101.vortex.navigation.current
import io.github.excu101.vortex.navigation.stack

open class NavigationHostPageController(
    private val context: Context,
    val graph: NavPageControllerGraph = NavPageControllerGraph(context),
) : PageController(context),
    ControllerProvider,
    OnPageChangeListener,
    NavStackListener<PageController> {

    protected val adapter = NavigationPageAdapter(this)

    override fun getControllerCount(): Int {
        return graph.stack.size
    }

    @CallSuper
    override fun onStackChanged(stack: NavStack<PageController>) {
//        for (i in 0 until stack.size)
//            if (adapter.getItemPosition(stack[i]) == POSITION_NONE)
//                adapter.notifyControllerRemove(i)

        adapter.notifyDataSetChanged()
    }

    override fun onCreatePageController(position: Int): PageController {
        return graph.stack[position]
    }

    override fun onPrepareController(position: Int, controller: PageController) {

    }

    override fun onHideController(position: Int, controller: PageController) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val current = graph.stack.current
        val next = graph.stack[adapter.wrapIndex(position)]


    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onCreateView(context: Context): View {
        val root = FrameLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        val container = ViewPager(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        container.adapter = adapter
        container.addOnPageChangeListener(this)
        onCreateView(context, container)

        root.addView(container, 0)

        return root
    }

    fun onCreateView(context: Context, pager: ViewPager) {

    }

    override fun onSaveInstance(bundle: Bundle, prefix: String): Boolean {
        graph.saveNavigation(bundle)
        return false
    }

    override fun onRestoreInstance(bundle: Bundle, prefix: String): Boolean {
        graph.restoreNavigation(bundle)
        return false
    }

    override fun onBackActivated(): Boolean = graph.controller.isBackActivated()

    override fun onDestroy() {
        super.onDestroy()
    }
}