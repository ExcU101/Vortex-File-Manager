package io.github.excu101.vortex.ui.screen.main

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import io.github.excu101.vortex.data.NavigationDrawerHeaderItem
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.drawer.BottomNavigationDrawerLayout
import io.github.excu101.vortex.ui.component.fragment.FragmentContainerView
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.utils.navigationDrawerHeader

class MainLayoutBinding(
    context: Context,
) : ViewBinding {

    override var root: BottomNavigationDrawerLayout = BottomNavigationDrawerLayout(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        adapter.add(ItemViewTypes.navigationDrawerHeader to NavigationDrawerHeaderItem as ViewHolderFactory<Item<*>>)
    }

    var bar: Bar = Bar(context).apply {

    }

    val container: FragmentContainerView = FragmentContainerView(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    var screenImage: ImageView = ImageView(context).apply {
        visibility = View.GONE
    }

    init {
        onCreate()
    }

    override fun onCreate() {
        root.apply {
            updateNavigationPaddings(
                bottom = 56.dp
            )

            bindOnSlideCallback { view, offset ->
                bar.navigationIcon.progress = offset
            }

            replaceContent(
                container,
                bar to CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    gravity = Gravity.BOTTOM
                },
                screenImage to CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            )
        }
    }

    override fun onDestroy() {
        root.removeAllViews()
    }

}