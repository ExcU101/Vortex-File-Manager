package io.github.excu101.vortex.theme.widget

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout
import io.github.excu101.vortex.theme.Theme
import io.github.excu101.vortex.theme.ThemeColorChangeListener

abstract class ThemeFrameLayout : FrameLayout, ThemeColorChangeListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(
        context,
        attrs,
        defStyleAttr
    )

    abstract override fun onColorChanged()

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        for (i in 0 until childCount) {
            getChildAt(i).dispatchApplyWindowInsets(insets)
        }
        return insets
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Theme.registerColorChangeListener(listener = this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Theme.unregisterColorChangeListener(listener = this)
    }
}