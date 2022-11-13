package io.github.excu101.vortex.ui.screen.storage.pager.page.list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.list.scroll.FastScroller
import io.github.excu101.vortex.ui.component.loading.LoadingView
import io.github.excu101.vortex.ui.component.storage.StorageListView
import io.github.excu101.vortex.ui.component.trail.TrailListView
import io.github.excu101.vortex.ui.component.warning.WarningView

class StorageListPageBinding(
    context: Context,
) : ViewBinding {

    override var root: CoordinatorLayout = CoordinatorLayout(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }
    var list: StorageListView = StorageListView(context).apply {
        visibility = View.GONE
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    val trail: TrailListView = TrailListView(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    var loading: LoadingView = LoadingView(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            gravity = Gravity.CENTER
        }
    }

    val warning: WarningView = WarningView(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            gravity = Gravity.CENTER
        }
    }
    val scroller: FastScroller = FastScroller(context).apply {

    }

    init {
        onCreate()
    }

    override fun onCreate() {
        root.addView(list)
        root.addView(scroller)
        root.addView(trail)
        root.addView(loading)
        root.addView(warning)

    }

    override fun onDestroy() {
        root.removeAllViews()
    }

}