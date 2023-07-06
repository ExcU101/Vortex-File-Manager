package io.github.excu101.vortex.navigation.page

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import androidx.annotation.CallSuper
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import io.github.excu101.vortex.navigation.NavStack
import io.github.excu101.vortex.navigation.NavStackListener
import io.github.excu101.vortex.navigation.NavigationPageAdapter
import io.github.excu101.vortex.navigation.NavigationPageAdapter.ControllerProvider

abstract class NavigationHostPageController(
    private val context: Context,
) : PageController(context),
    ControllerProvider,
    OnPageChangeListener,
    NavStackListener<PageController> {

    protected val adapter = NavigationPageAdapter(this)
    protected var root: FrameLayout? = null
    protected var container: ViewPager? = null

    val currentController: PageController?
        get() = adapter.getController(adapter.wrapIndex(container?.currentItem ?: 0))

    @CallSuper
    override fun onStackChanged(stack: NavStack<PageController>) {
//        for (i in 0 until stack.size)
//            if (adapter.getItemPosition(stack[i]) == POSITION_NONE)
//                adapter.notifyControllerRemove(i)

        adapter.notifyDataSetChanged()
    }

    override fun onPrepareController(position: Int, controller: PageController) {

    }

    override fun onHideController(position: Int, controller: PageController) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onCreateView(context: Context): View {
        root = FrameLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        container = ViewPager(context).apply {
            addOnPageChangeListener(this@NavigationHostPageController)
            adapter = this@NavigationHostPageController.adapter
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        onCreateView(context, container!!)

        root?.addView(container, 0)

        return root!!
    }

    protected open fun onCreateView(context: Context, pager: ViewPager) {

    }

    override fun onActivityResult(request: Int, result: Int, data: Intent?): Boolean {
        return currentController?.onActivityResult(request, result, data) ?: false
    }

    override fun onBackActivated(): Boolean {
        return currentController?.onBackActivated() ?: false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isDestroyed) {
            root?.removeAllViews()
            root = null
            container = null
        }
    }
}