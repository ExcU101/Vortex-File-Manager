package io.github.excu101.vortex.ui.component.fragment

import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.core.view.forEach
import androidx.core.view.get

class FragmentContainerView(
    context: Context,
) : FrameLayout(context) {

    init {
        id = View.generateViewId()
    }

    private val transitionFragmentView = mutableListOf<View>()
    private val disappearingFragmentView = mutableListOf<View>()

    private var drawDisappearingViewsFirst = true

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        forEach { view ->
            view.dispatchApplyWindowInsets(WindowInsets(insets))
        }
        return insets
    }

    override fun startViewTransition(view: View) {
        if (view.parent == this) {
            transitionFragmentView.add(view)
        }
        super.startViewTransition(view)
    }

    override fun endViewTransition(view: View) {
        transitionFragmentView.remove(view)
        if (disappearingFragmentView.remove(view)) {
            drawDisappearingViewsFirst = true
        }
        super.endViewTransition(view)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (drawDisappearingViewsFirst) {
            for (view in disappearingFragmentView) {
                super.drawChild(canvas, view, drawingTime);
            }
        }
        super.dispatchDraw(canvas)
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        if (drawDisappearingViewsFirst && disappearingFragmentView.size > 0) {
            if (disappearingFragmentView.contains(child)) {
                return false
            }
        }
        return super.drawChild(canvas, child, drawingTime)
    }

    override fun removeViewAt(index: Int) {
        addDisappearingFragmentView(get(index))
        super.removeViewAt(index)
    }

    override fun removeViewInLayout(view: View) {
        addDisappearingFragmentView(view)
        super.removeViewInLayout(view)
    }

    override fun removeView(view: View) {
        addDisappearingFragmentView(view)
        super.removeView(view)
    }

    override fun removeViews(start: Int, count: Int) {
        for (i in start until start + count) {
            val view = getChildAt(i)
            addDisappearingFragmentView(view)
        }
        super.removeViews(start, count)
    }

    override fun removeViewsInLayout(start: Int, count: Int) {
        for (i in start until start + count) {
            val view = getChildAt(i)
            addDisappearingFragmentView(view)
        }
        super.removeViewsInLayout(start, count)
    }

    override fun removeAllViewsInLayout() {
        forEach { view ->
            addDisappearingFragmentView(view)
        }
        super.removeAllViewsInLayout()
    }

    override fun removeDetachedView(child: View, animate: Boolean) {
        if (animate) {
            addDisappearingFragmentView(child)
        }
        super.removeDetachedView(child, animate)
    }

    private fun addDisappearingFragmentView(view: View) {
        if (view.animation != null && transitionFragmentView.contains(view)) {
            disappearingFragmentView.add(view)
        }
    }
}