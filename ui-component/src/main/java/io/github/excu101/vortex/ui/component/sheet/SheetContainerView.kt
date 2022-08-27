package io.github.excu101.vortex.ui.component.sheet

import android.content.Context
import android.view.View.MeasureSpec.getSize
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.core.view.WindowInsetsCompat

class SheetContainerView(context: Context) : FrameLayout(context) {

    var insets: WindowInsets? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getSize(widthMeasureSpec)
        val height = getSize(heightMeasureSpec)
        var outPutHeight = height

        insets?.let {
            val insetBottom = WindowInsetsCompat.toWindowInsetsCompat(it)
                .getInsets(WindowInsetsCompat.Type.systemBars()).bottom

            outPutHeight -= insetBottom
        }

        setMeasuredDimension(width, outPutHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

}