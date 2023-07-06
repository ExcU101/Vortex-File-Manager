package io.github.excu101.vortex.ui.component.loading

import android.content.Context
import android.view.Gravity.CENTER
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.progressindicator.CircularProgressIndicator
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.theme.key.layoutProgressBarTintColorKey
import io.github.excu101.vortex.theme.key.layoutProgressTitleTextColorKey

class LoadingView(context: Context) : io.github.excu101.vortex.theme.widget.ThemeLinearLayout(context) {

    private val progressView = CircularProgressIndicator(context).apply {
        isIndeterminate = true
        setIndicatorColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.layoutProgressBarTintColorKey))
    }
    private val titleView = TextView(context).apply {
        textSize = 18F
        setTextColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.layoutProgressTitleTextColorKey))
        textAlignment = TEXT_ALIGNMENT_CENTER
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        setPadding(
            0,
            16.dp,
            0,
            0
        )
    }

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
        orientation = VERTICAL
        gravity = CENTER

        addView(progressView)
    }

    private fun ensureContainingTitle() {
        if (!containsTitleView) {
            addView(titleView)
        }
    }

    override fun onColorChanged() {
//        background.setTint(ThemeColor(layoutProgressBarBackgroundColorKey))
        progressView.setIndicatorColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.layoutProgressBarTintColorKey))
        titleView.setTextColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.layoutProgressTitleTextColorKey))
    }

}