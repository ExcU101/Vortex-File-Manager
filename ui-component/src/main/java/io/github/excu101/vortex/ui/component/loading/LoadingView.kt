package io.github.excu101.vortex.ui.component.loading

import android.content.Context
import android.view.Gravity.CENTER
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.shape.MaterialShapeDrawable
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.layoutProgressBarBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.layoutProgressBarTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.layoutProgressTitleTextColorKey

class LoadingView(context: Context) : ThemeLinearLayout(context) {

    private val progressView = CircularProgressIndicator(context).apply {
        isIndeterminate = true
        setIndicatorColor(ThemeColor(layoutProgressBarTintColorKey))
    }
    private val titleView = TextView(context).apply {
        textSize = 18F
        setTextColor(ThemeColor(layoutProgressTitleTextColorKey))
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

    override fun onChanged() {
//        background.setTint(ThemeColor(layoutProgressBarBackgroundColorKey))
        progressView.setIndicatorColor(ThemeColor(layoutProgressBarTintColorKey))
        titleView.setTextColor(ThemeColor(layoutProgressTitleTextColorKey))
    }

}