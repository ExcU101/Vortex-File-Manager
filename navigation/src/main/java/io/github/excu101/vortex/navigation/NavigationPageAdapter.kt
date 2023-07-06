package io.github.excu101.vortex.navigation

import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.viewpager.widget.PagerAdapter
import io.github.excu101.vortex.navigation.page.PageController

class NavigationPageAdapter(
    private val provider: ControllerProvider,
) : PagerAdapter() {

    interface ControllerProvider {
        fun getControllerCount(): Int
        fun onCreatePageController(position: Int): PageController
        fun onPrepareController(position: Int, controller: PageController)
        fun onHideController(position: Int, controller: PageController)
    }

    var isRtl = false

    private val cache = SparseArrayCompat<PageController>()

    fun wrapIndex(position: Int): Int {
        return if (isRtl) provider.getControllerCount() - 1 - position else position
    }

    fun getController(index: Int): PageController? {
        if (index < 0 || index >= cache.size()) return null
        return cache.valueAt(index)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val controller = cache.valueAt(position)

        return controller.title
    }

    override fun getCount(): Int {
        return provider.getControllerCount()
    }

    fun contains(controller: PageController): Boolean {
        return cache.containsValue(controller)
    }

    fun notifyControllerInserted(index: Int) {
        for (i in cache.size() - 1 downTo 0) {
            val key = cache.keyAt(i)
            if (key < index) break
            with(cache.valueAt(i)) {
                cache.removeAt(i)
                cache.put(key + 1, this)
            }
        }
    }

    fun notifyControllerRemove(index: Int) {
        var i = cache.indexOfKey(index)
        if (i < 0) return
        val c = cache.valueAt(i)
        cache.removeAt(i)
        c.onDestroy()
        val count = cache.size()

        while (i < count) {
            val key = cache.keyAt(i)
            val item = cache.valueAt(i)
            cache.removeAt(i)
            cache.put(key - 1, item)
            i++
        }
    }

    fun destroy() {
        for (i in 0 until cache.size()) {
            val controller = cache.valueAt(i)

            if (!controller.isDestroyed)
                controller.onDestroy()
        }
        cache.clear()
    }

    override fun getItemPosition(item: Any): Int {
        for (i in 0 until cache.size())
            if (cache.valueAt(i) == item) return cache.keyAt(i)
        return POSITION_NONE
    }

    private fun checkController(position: Int): PageController {
        var controller = cache[position]

        if (controller == null) {
            controller = provider.onCreatePageController(position)
            cache.put(position, controller)
        }

        return controller
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val controller = checkController(position)
        provider.onPrepareController(position, controller)
        controller.onPrepare()
        container.addView(controller.getContentView())
        return controller
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        item as PageController
        container.removeView(item.getContentView())
        provider.onHideController(position, item)
        item.onHide()
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        return item is PageController && item.getContentView() == view
    }
}