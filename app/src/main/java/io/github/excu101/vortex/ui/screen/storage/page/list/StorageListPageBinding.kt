package io.github.excu101.vortex.ui.screen.storage.page.list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.view.updateLayoutParams
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.info.InfoView
import io.github.excu101.vortex.ui.component.list.scroll.FastScroller
import io.github.excu101.vortex.ui.component.loading.LoadingView
import io.github.excu101.vortex.ui.component.storage.StorageListView
import io.github.excu101.vortex.ui.component.trail.TrailListView
import io.github.excu101.vortex.ui.icon.Icons

class StorageListPageBinding(
    context: Context,
) : ViewBinding<CoordinatorLayout> {

    override var root: CoordinatorLayout = CoordinatorLayout(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    private class FabBehavior : Behavior<ExtendedFloatingActionButton>() {
        override fun layoutDependsOn(
            parent: CoordinatorLayout,
            child: ExtendedFloatingActionButton,
            dependency: View,
        ): Boolean {
            return dependency is Bar
        }

        override fun onDependentViewChanged(
            parent: CoordinatorLayout,
            child: ExtendedFloatingActionButton,
            dependency: View,
        ): Boolean {
            return when (dependency) {
                is Bar -> {
                    child.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        rightMargin = with(child) { 16.dp }
                        bottomMargin =
                            dependency.height - dependency.translationY.toInt() + with(child) { 16.dp }
                    }
                    true
                }
                else -> super.onDependentViewChanged(parent, child, dependency)
            }
        }
    }

    var fab = ExtendedFloatingActionButton(context).apply {
        icon = Icons.Rounded.ContentPaste
        text = "Tasks"
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.END or Gravity.BOTTOM
            behavior = FabBehavior()
        }
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