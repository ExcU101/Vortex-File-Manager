package io.github.excu101.vortex.ui.component.sheet

import android.content.Context
import android.view.Gravity.*
import android.view.VelocityTracker
import android.view.View
import android.view.View.MeasureSpec.*
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.children
import io.github.excu101.vortex.ui.component.foundtation.ListenerRegister

class SheetContainer(
    context: Context,
) : FrameLayout(context), ListenerRegister<SheetContainerListener>, NestedScrollingParent {

    private val listeners = mutableListOf<SheetContainerListener>()

    private val tracker = VelocityTracker.obtain()
    private val helper = NestedScrollingParentHelper(this)

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        helper.onNestedScrollAccepted(child, target, axes)
        notifyListeners { listener ->
            listener.onScrollAccepted()
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        notifyListeners { listener ->
            listener.onNestedPreScroll(
                dx = dx,
                dy = dy,
                consumed = consumed
            )
        }
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean,
    ): Boolean {
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun getNestedScrollAxes(): Int {
        return helper.nestedScrollAxes
    }

    override fun registerListener(listener: SheetContainerListener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: SheetContainerListener) {
        listeners.remove(listener)
    }

    private inline fun notifyListeners(
        action: (listener: SheetContainerListener) -> Unit,
    ) {
        listeners.forEach(action)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getSize(widthMeasureSpec)
        val height = getSize(heightMeasureSpec)
        var realHeight = height
        setMeasuredDimension(width, height)

        for (view in children) {
            if (view.visibility == GONE) {
                continue
            }
            measureChildWithMargins(view,
                makeMeasureSpec(width, EXACTLY),
                0,
                makeMeasureSpec(height, EXACTLY),
                0
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (view in children) {
            if (view.visibility == GONE) {
                continue
            }

            val params = view.layoutParams as LayoutParams
            val width = view.measuredWidth
            val height = view.measuredHeight

            var gravity = params.gravity
            if (gravity == -1) {
                gravity = TOP or LEFT
            }

            val absoluteHorizontalGravity = gravity and HORIZONTAL_GRAVITY_MASK
            val absoluteVerticalGravity = gravity and VERTICAL_GRAVITY_MASK

            val viewStart: Int = when (absoluteHorizontalGravity and HORIZONTAL_GRAVITY_MASK) {
                CENTER_HORIZONTAL -> {
                    (right - left - width) / 2 + params.leftMargin - params.rightMargin
                }
                RIGHT -> {
                    right - width - params.rightMargin
                }
                else -> params.leftMargin
            }

            val viewTop: Int = when (absoluteVerticalGravity) {
                CENTER_VERTICAL -> {
                    (bottom - top - height) / 2 + params.topMargin - params.bottomMargin
                }
                BOTTOM -> {
                    (bottom - top) - height - params.bottomMargin
                }
                else -> {
                    params.topMargin
                }
            }
            val viewEnd: Int = viewStart + width
            val viewBottom: Int = viewTop + height

            view.layout(viewStart, viewTop, viewEnd, viewBottom)
        }
    }

}