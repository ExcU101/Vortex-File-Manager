package io.github.excu101.vortex.navigation.page

import android.content.Context
import android.widget.FrameLayout

class NavigationPageLayout(context: Context) : FrameLayout(context) {
    var preventLayout = false
    var layoutRequested = false
    var layoutLimit = 0
        set(value) {
            field = value
            layoutCompleted = 0
        }
    private var layoutCompleted = 0

    fun layoutIfRequested() {
        preventLayout = false
        if (layoutRequested) {
            layoutRequested = false
            requestLayout()
        }
    }

    fun cancelLayout() {
        preventLayout = false
        layoutRequested = false
    }

    override fun requestLayout() {
        super.requestLayout()
//        if (!preventLayout) {
//            if (layoutLimit == -1) {
//                super.requestLayout()
//            } else if (layoutCompleted < layoutLimit) {
//                layoutCompleted++
//                super.requestLayout()
//            }
//        } else {
//            layoutRequested = true
//        }
    }

    fun completeNextLayout() {
        layoutLimit = -1
        layoutCompleted = 0
    }
}