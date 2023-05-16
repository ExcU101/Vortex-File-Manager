package io.github.excu101.vortex.ui.component.menu

import android.content.Context
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.getMode
import android.view.View.MeasureSpec.getSize
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.FrameLayout
import io.github.excu101.vortex.ui.component.dp
import java.lang.Integer.max
import kotlin.math.min

class MenuLayout(context: Context) : FrameLayout(context) {

    private val items = mutableListOf<MenuAction>()
    private val views = mutableListOf<MenuItem>()

    private val listeners = mutableListOf<MenuActionListener>()

    val isVertical: Boolean
        get() = false

    fun addItem(action: MenuAction) {
        items += action
        initViews()
    }

    fun removeItem(action: MenuAction) {
        items -= action
        initViews()
    }

    fun replaceItems(actions: Collection<MenuAction>) {
        items.clear()
        items.addAll(actions)
        initViews()
    }

    init {
        initViews()
    }

    fun addListener(listener: MenuActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: MenuActionListener) {
        listeners.remove(listener)
    }

    private fun initViews() {
        removeAllViews()
        views.clear()
        for (item in items) {
            views.add(getItem(item))
        }
        for (view in views) {
            addView(view)
        }
    }

    private fun getItem(item: MenuAction): MenuItem {
        return MenuItem(context).apply {
            action = item
            setOnClickListener { view ->
                notify(item)
            }
        }
    }

    private fun notify(item: MenuAction) {
        for (listener in listeners) {
            if (listener.onMenuActionCall(item)) break
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec) // requires full width-size
        val widthMode = getMode(widthMeasureSpec)
        val heightSize = getSize(heightMeasureSpec)
        val heightMode = getMode(heightMeasureSpec)

        val width = when (widthMode) {
            EXACTLY -> widthSize
            AT_MOST -> max(56.dp, widthSize)
            else -> 56.dp
        }


        val height = when (heightMode) {
            EXACTLY -> heightSize
            AT_MOST -> min(56.dp, heightSize)
            else -> 56.dp
        }

        views.forEach { item ->
            item.measure(
                makeMeasureSpec(24.dp, EXACTLY),
                makeMeasureSpec(24.dp, EXACTLY)
            )
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var leftWidth = measuredWidth - 16.dp
        var topHeight = (if (isVertical) 0 else height / 2)

        views.forEachIndexed { index, item ->
            item.layout(
                leftWidth - item.measuredWidth,
                height / 2 - item.measuredHeight,
                leftWidth,
                height / 2 + item.measuredHeight
            )
            leftWidth = leftWidth - item.measuredWidth - 24.dp
        }
    }

}