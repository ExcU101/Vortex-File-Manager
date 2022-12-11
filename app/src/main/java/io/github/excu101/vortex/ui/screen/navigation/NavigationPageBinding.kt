package io.github.excu101.vortex.ui.screen.navigation

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import io.github.excu101.vortex.ui.component.FragmentAdapter
import io.github.excu101.vortex.ui.component.bar.Bar

class NavigationPageBinding(
    private val context: Context,
    private val adapter: FragmentAdapter
) {

    var root: FrameLayout? = null
    var bar: Bar? = null
    var pager: ViewPager2? = null

    fun onCreate(): View? {
        root = FrameLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        bar = Bar(context).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                gravity = Gravity.BOTTOM
            }
        }

        pager = ViewPager2(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            adapter = this@NavigationPageBinding.adapter
        }

        root?.addView(pager)
        root?.addView(bar)

        return root
    }

    fun onDestroy() {
        root = null
        bar = null
        pager = null
    }


}