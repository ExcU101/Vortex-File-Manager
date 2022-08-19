package io.github.excu101.vortex.ui.view.storage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updatePadding
import com.google.android.material.bottomappbar.BottomAppBar
import io.github.excu101.vortex.ui.view.bar.BottomBar
import io.github.excu101.vortex.ui.view.trail.TrailView

class StorageListViewBehavior : CoordinatorLayout.Behavior<StorageListView> {

    constructor() : super()

    constructor(
        context: Context?,
        attrs: AttributeSet?,
    ) : super(
        context,
        attrs
    )

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: StorageListView,
        dependency: View,
    ): Boolean {
        return when (dependency) {
            is TrailView -> {
                child.updatePadding(top = dependency.height + dependency.translationY.toInt())
                true
            }
            else -> super.onDependentViewChanged(parent, child, dependency)
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: StorageListView,
        dependency: View,
    ): Boolean {
        return dependency is TrailView || dependency is BottomBar
    }
}