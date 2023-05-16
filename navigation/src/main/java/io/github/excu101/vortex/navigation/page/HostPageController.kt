package io.github.excu101.vortex.navigation.page

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.collection.SparseArrayCompat
import androidx.viewpager.widget.ViewPager

abstract class HostPageController(
    context: Context,
) : PageController(context), ViewPager.OnPageChangeListener {

    private var _root: FrameLayout? = null
    private var _pager: ViewPager? = null
    private var _adapter: PagerAdapter? = null
    protected val adapter
        get() = _adapter

    protected abstract fun getPageCount(): Int
    protected abstract fun onCreatePage(context: Context, position: Int): PageController

    override fun onCreateView(context: Context): View {
        _adapter = PagerAdapter(host = this)
        _root = FrameLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        _pager = ViewPager(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            adapter = _adapter
            addOnPageChangeListener(this@HostPageController)
        }

        _root!!.addView(_pager!!)
        onCreateView(_pager!!)

        return _root!!
    }

    open fun onCreateView(pager: ViewPager) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    class PagerAdapter(
        private val host: HostPageController,
    ) : androidx.viewpager.widget.PagerAdapter() {

        private val _cache = SparseArrayCompat<PageController>(host.getPageCount())

        var isRtl: Boolean = false

        private fun createPageController(context: Context, position: Int): PageController {
            var controller = _cache[position]
            if (controller == null) {
                controller = host.onCreatePage(context, position)
                _cache.put(position, controller)
            }
            return controller
        }

        private fun checkRtl(position: Int): Int {
            return if (isRtl) count - 1 - position else position
        }

        override fun getCount(): Int = host.getPageCount()

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return `object` is PageController && `object`.getContentView() == view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            if (`object` !is PageController) return

            container.removeView(`object`.getContentView())
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val controller = createPageController(container.context, position)
            container.addView(controller.getContentView())
            return controller
        }

        override fun getItemPosition(`object`: Any): Int {
            for (i in 0 until _cache.size())
                if (_cache.valueAt(i) === `object`) return checkRtl(_cache.keyAt(i))

            return POSITION_NONE
        }
    }

}