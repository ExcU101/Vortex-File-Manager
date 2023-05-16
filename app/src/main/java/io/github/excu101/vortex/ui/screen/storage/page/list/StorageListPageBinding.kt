package io.github.excu101.vortex.ui.screen.storage.page.list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.manager.ui.theme.ThemeDimen
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.info.InfoView
import io.github.excu101.vortex.ui.component.list.scroll.FastScroller
import io.github.excu101.vortex.ui.component.loading.LoadingView
import io.github.excu101.vortex.ui.component.theme.key.storageListBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.trailItemHeightKey
import io.github.excu101.vortex.ui.component.trail.TrailListView
import io.github.excu101.vortex.ui.component.udp

class StorageListPageBinding(
    context: Context,
) : ViewBinding<FrameLayout> {

    override var root: FrameLayout = FrameLayout(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setBackgroundColor(ThemeColor(storageListBackgroundColorKey))
    }

    var list: RecyclerView = RecyclerView(
        context,
    ).apply {
        visibility = View.VISIBLE
//        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)

        updatePadding(
            top = ThemeDimen(trailItemHeightKey).udp,
        )

        setHasFixedSize(true)
        isNestedScrollingEnabled = true
        clipToPadding = false

        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    val trail: TrailListView = TrailListView(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    var loading: LoadingView = LoadingView(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            gravity = Gravity.CENTER
        }
    }

    val warning: InfoView = InfoView(context).apply {
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