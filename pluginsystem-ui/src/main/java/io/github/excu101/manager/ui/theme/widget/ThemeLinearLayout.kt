package io.github.excu101.manager.ui.theme.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import io.github.excu101.manager.ui.theme.Theme
import io.github.excu101.manager.ui.theme.ThemeColorChangeListener

abstract class ThemeLinearLayout : LinearLayout, ThemeColorChangeListener {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Theme.registerColorChangeListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Theme.unregisterColorChangeListener(this)
    }

    abstract override fun onColorChanged()

}