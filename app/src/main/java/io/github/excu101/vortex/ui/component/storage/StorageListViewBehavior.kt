package io.github.excu101.vortex.ui.component.storage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updatePadding
import io.github.excu101.vortex.ui.component.trail.TrailListView

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
            is TrailListView -> {
                child.updatePadding(
                    top = dependency.height + dependency.translationY.toInt(),
                )
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
        return dependency is TrailListView
    }
}