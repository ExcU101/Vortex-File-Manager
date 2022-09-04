package io.github.excu101.vortex.ui.component.loading

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.view.View.MeasureSpec.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.shape.MaterialShapeDrawable
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.layoutProgressBarBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.layoutProgressBarTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.layoutProgressTitleTextColorKey

class LoadingView(context: Context) : FrameLayout(context) {

    private val background = MaterialShapeDrawable().apply {
        fillColor = valueOf(ThemeColor(layoutProgressBarBackgroundColorKey))
    }

    private val progressView = CircularProgressIndicator(context).apply {
        isIndeterminate = true
        setIndicatorColor(ThemeColor(layoutProgressBarTintColorKey))
    }
    private val titleView = TextView(context).apply {
        setTextColor(ThemeColor(layoutProgressTitleTextColorKey))
    }

    private val circularSize = 24.dp

    var title: CharSequence?
        get() = titleView.text
        set(value) {
            ensureContainingTitle()
            titleView.text = value
        }

    private val containsProgressView
        get() = progressView in children

    private val containsTitleView
        get() = titleView in children

    init {
        setBackground(background)

        addView(progressView)
    }

    private fun ensureContainingTitle() {
        if (!containsTitleView) {
            addView(titleView)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getSize(widthMeasureSpec)
        val height = getSize(heightMeasureSpec)

        setMeasuredDimension(width, height)

        val availableWidth = width
        val availableHeight = width - 24.dp
        progressView.measure(
            makeMeasureSpec(24.dp, AT_MOST),
            makeMeasureSpec(24.dp, AT_MOST)
        )
        titleView.measure(
            makeMeasureSpec(availableWidth, AT_MOST),
            makeMeasureSpec(availableHeight, AT_MOST)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (containsProgressView) {
            progressView.layout(
                measuredWidth / 2 - progressView.measuredWidth,
                measuredHeight / 2 - progressView.measuredHeight,
                measuredWidth / 2 + progressView.measuredWidth,
                measuredHeight / 2 + progressView.measuredHeight,
            )
        }
        if (containsTitleView) {
            progressView.layout(
                measuredWidth / 2 - titleView.measuredWidth,
                measuredHeight / 2 + progressView.measuredHeight - titleView.measuredHeight,
                measuredWidth / 2 + titleView.measuredWidth,
                measuredHeight / 2 + progressView.measuredHeight + titleView.measuredHeight,
            )
        }
    }

}